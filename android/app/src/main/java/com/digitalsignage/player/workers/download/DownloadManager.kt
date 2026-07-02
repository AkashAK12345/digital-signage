package com.digitalsignage.player.workers.download

import com.digitalsignage.player.core.event.PlayerEvent
import com.digitalsignage.player.core.event.PlayerEventBus
import com.digitalsignage.player.core.logging.Logger
import com.digitalsignage.player.core.storage.StorageManager
import com.digitalsignage.player.core.utils.FileValidator
import com.digitalsignage.player.data.local.AppDatabase
import com.digitalsignage.player.data.local.DownloadSessionEntity
import com.digitalsignage.player.domain.model.DownloadState
import com.digitalsignage.player.domain.model.PlaylistState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadManager @Inject constructor(
    private val database: AppDatabase,
    private val storageManager: StorageManager,
    private val fileValidator: FileValidator,
    private val eventBus: PlayerEventBus,
    private val logger: Logger
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val client = OkHttpClient()
    private val downloadMutex = Mutex()
    private var isRunning = false
    
    // Future Bandwidth Management Configurations
    private val maxConcurrentDownloads = 3
    private val enableThrottling = false
    private val enablePrefetch = true

    fun startProcessing() {
        scope.launch {
            downloadMutex.withLock {
                if (isRunning) return@launch
                isRunning = true
            }
            processQueue()
        }
    }
    
    private suspend fun processQueue() {
        while (isRunning) {
            val pendingSessions = database.downloadSessionDao().getPendingTasks()
            if (pendingSessions.isEmpty()) {
                isRunning = false
                break
            }
            
            if (!storageManager.isStorageAvailable()) {
                logger.e("DownloadManager", "Storage full. Pausing downloads.")
                pendingSessions.forEach {
                    database.downloadSessionDao().updateSessionState(it.mediaId, DownloadState.PAUSED, System.currentTimeMillis())
                }
                isRunning = false
                return
            }
            
            // Process concurrently up to maxConcurrentDownloads
            val batch = pendingSessions.take(maxConcurrentDownloads)
            
            // Await all downloads in this batch
            batch.map { session ->
                scope.async { attemptDownload(session) }
            }.awaitAll()
        }
    }
    
    private suspend fun attemptDownload(session: DownloadSessionEntity) {
        val maxRetries = 5
        val now = System.currentTimeMillis()
        
        if (session.retryCount >= maxRetries) {
            logger.e("DownloadManager", "Max retries reached for ${session.mediaId}")
            database.downloadSessionDao().updateSessionState(session.mediaId, DownloadState.FAILED, now)
            eventBus.publish(PlayerEvent.DownloadFailed(session.mediaId, Exception("Max retries exceeded")))
            return
        }
        
        try {
            database.downloadSessionDao().updateSessionState(session.mediaId, DownloadState.DOWNLOADING, now)
            eventBus.publish(PlayerEvent.DownloadStarted(session.mediaId))
            
            val destFile = File(storageManager.getMediaDirectory(), session.mediaId)
            val tempFile = File(storageManager.getMediaDirectory(), "${session.mediaId}.tmp")
            
            var downloadedBytes = session.currentByteOffset
            if (tempFile.exists() && downloadedBytes == 0L) {
                downloadedBytes = tempFile.length()
            }
            
            val requestBuilder = Request.Builder().url(session.url)
            if (downloadedBytes > 0) {
                requestBuilder.header("Range", "bytes=${downloadedBytes}-")
                logger.d("DownloadManager", "Resuming download for ${session.mediaId} from offset: ${downloadedBytes}")
            }
            
            val response = client.newCall(requestBuilder.build()).execute()
            
            if (!response.isSuccessful && response.code != 206) {
                throw Exception("HTTP ${response.code}")
            }
            
            val body = response.body
            if (body != null) {
                val contentLength = body.contentLength()
                val expectedSize = if (contentLength > 0) contentLength + downloadedBytes else null
                
                val inputStream: InputStream = body.byteStream()
                val outputStream = FileOutputStream(tempFile, downloadedBytes > 0)
                
                val buffer = ByteArray(8192)
                var read: Int
                var totalRead = downloadedBytes
                
                var lastReportedProgress = 0
                var lastDbSync = System.currentTimeMillis()
                
                while (inputStream.read(buffer).also { read = it } != -1) {
                    if (enableThrottling) {
                        // Sleep slightly to throttle bandwidth (placeholder logic)
                        // delay(5)
                    }
                    
                    outputStream.write(buffer, 0, read)
                    totalRead += read
                    
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastDbSync > 5000) {
                        database.downloadSessionDao().updateSessionOffset(session.mediaId, totalRead, currentTime)
                        lastDbSync = currentTime
                    }
                    
                    if (expectedSize != null && expectedSize > 0) {
                        val progress = ((totalRead * 100) / expectedSize).toInt()
                        if (progress - lastReportedProgress >= 5) {
                            lastReportedProgress = progress
                            eventBus.publish(PlayerEvent.DownloadProgress(session.mediaId, progress))
                        }
                    }
                }
                outputStream.flush()
                outputStream.close()
                inputStream.close()
                
                database.downloadSessionDao().updateSessionOffset(session.mediaId, totalRead, System.currentTimeMillis())
                
                // Validate Hash
                if (fileValidator.validateFile(tempFile, session.expectedChecksumMd5, session.expectedChecksumSha256, expectedSize)) {
                    tempFile.renameTo(destFile)
                    database.downloadSessionDao().updateSessionState(session.mediaId, DownloadState.COMPLETED, System.currentTimeMillis())
                    database.playlistDao().updateMediaDownloadedState(session.mediaId, true, destFile.absolutePath)
                    
                    eventBus.publish(PlayerEvent.DownloadCompleted(session.mediaId))
                    checkPlaylistReadiness()
                } else {
                    tempFile.delete()
                    database.downloadSessionDao().updateSessionOffset(session.mediaId, 0L, System.currentTimeMillis())
                    throw Exception("Checksum or Size validation failed")
                }
            } else {
                throw Exception("Empty response body")
            }
        } catch (e: Exception) {
            logger.e("DownloadManager", "Failed downloading ${session.mediaId}", e)
            val nextRetry = session.retryCount + 1
            database.downloadSessionDao().incrementRetryCount(session.mediaId, nextRetry, System.currentTimeMillis())
            database.downloadSessionDao().updateSessionState(session.mediaId, DownloadState.QUEUED, System.currentTimeMillis())
            delay(2000L * nextRetry)
        }
    }
    
    private suspend fun checkPlaylistReadiness() {
        val pendingPlaylist = database.playlistDao().getPlaylistByState(PlaylistState.PENDING)
        if (pendingPlaylist != null) {
            val incompleteCount = database.playlistDao().countIncompleteMediaItems(pendingPlaylist.playlistId)
            if (incompleteCount == 0) {
                logger.i("DownloadManager", "All media downloaded. Activating playlist: ${pendingPlaylist.playlistId}")
                database.playlistDao().archiveActivePlaylist()
                database.playlistDao().activatePendingPlaylist(pendingPlaylist.playlistId)
                
                val activeItems = database.playlistDao().getMediaItemsForPlaylist(pendingPlaylist.playlistId)
                storageManager.cleanupOrphans(activeItems.map { it.mediaId })
                
                eventBus.publish(PlayerEvent.PlaylistReady)
            }
        }
    }
}

