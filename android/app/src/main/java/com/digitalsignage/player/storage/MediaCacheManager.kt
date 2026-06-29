package com.digitalsignage.player.storage

import android.content.Context
import com.digitalsignage.player.BuildConfig
import com.digitalsignage.player.DigitalSignageApplication
import com.digitalsignage.player.network.RetrofitClient
import com.digitalsignage.player.network.model.CurrentPlaylistResponse
import com.digitalsignage.player.network.model.PlaylistMediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

data class CacheStatus(
    val totalItems: Int,
    val cachedItems: Int,
    val currentDownload: String?,
    val downloadProgress: Int,
    val lastError: String?
)

interface MediaCacheListener {
    fun onCacheStatusChanged(status: CacheStatus)
}

class MediaCacheManager(private val context: Context) {

    companion object {
        private const val TAG = "MediaCache"
        private const val MEDIA_DIR = "media_cache"
        private const val DOWNLOAD_TIMEOUT_SECONDS = 120L
        private const val MAX_CACHE_SIZE_BYTES = 500L * 1024 * 1024 // 500 MB
        private const val SOFT_CACHE_SIZE_BYTES = 400L * 1024 * 1024 // 400 MB
    }

    private val logger = DigitalSignageApplication.logger
    private val mediaDir: File by lazy {
        File(context.filesDir, MEDIA_DIR).also { it.mkdirs() }
    }
    
    val integrityStats = MediaIntegrityStats()

    private val downloadClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(DOWNLOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()
                RetrofitClient.deviceToken?.let { token ->
                    builder.header("Authorization", "Bearer $token")
                }
                chain.proceed(builder.build())
            }
            .build()
    }

    private var listener: MediaCacheListener? = null

    fun setListener(listener: MediaCacheListener?) {
        this.listener = listener
    }

    fun getCachedFilePath(mediaId: String): File? {
        val files = mediaDir.listFiles { _, name -> name.startsWith("${mediaId}_") }
        return files?.firstOrNull()
    }

    fun getCachedFileCount(): Int {
        return mediaDir.listFiles()?.size ?: 0
    }

    suspend fun syncMedia(playlist: CurrentPlaylistResponse): Boolean {
        return withContext(Dispatchers.IO) {
            val requiredMediaIds = playlist.items.map { it.media.mediaId }.toSet()
            val totalItems = requiredMediaIds.size
            var cachedItems = 0
            var hasErrors = false

            logger.i(TAG, "Starting media sync for ${totalItems} items")

            // Download new or changed media sequentially
            for ((index, item) in playlist.items.distinctBy { it.media.mediaId }.withIndex()) {
                val media = item.media
                val cachedFile = getCachedFilePath(media.mediaId)

                if (isCacheValid(cachedFile, media)) {
                    cachedItems++
                    logger.d(TAG, "Cache hit: ${media.name} (${media.mediaId})")
                    notifyStatus(totalItems, cachedItems, null, 100, null)
                    continue
                }

                // Cache miss or stale — download
                logger.i(TAG, "Download started: ${media.name} (${media.mediaId})")
                notifyStatus(totalItems, cachedItems, media.name, 0, null)

                val result = downloadMedia(media)
                when (result) {
                    is DownloadResult.Success -> {
                        cachedItems++
                        logger.i(TAG, "Download completed: ${media.name} -> ${result.file.name}")
                        notifyStatus(totalItems, cachedItems, null, 100, null)
                    }
                    is DownloadResult.Error -> {
                        hasErrors = true
                        logger.e(TAG, "Download failed: ${media.name}: ${result.message}")
                        notifyStatus(totalItems, cachedItems, null, 0, result.message)
                    }
                }
            }

            // Delete obsolete cached files
            deleteObsoleteMedia(requiredMediaIds)
            
            // Enforce LRU bounds
            enforceCacheLimits(requiredMediaIds)
            
            integrityStats.lastSuccessfulVerification = System.currentTimeMillis()

            logger.i(TAG, "Media sync complete: $cachedItems/$totalItems cached, errors=$hasErrors")
            notifyStatus(totalItems, cachedItems, null, 100, null)
            !hasErrors
        }
    }

    private fun isCacheValid(cachedFile: File?, media: PlaylistMediaItem): Boolean {
        if (cachedFile == null || !cachedFile.exists()) return false
        if (cachedFile.length() == 0L) {
            logger.w(TAG, "Cached file empty, will re-download: ${media.mediaId}")
            cachedFile.delete()
            return false
        }

        // If the backend provides a checksum, validate it
        val serverChecksum = media.checksum
        if (!serverChecksum.isNullOrEmpty()) {
            val localChecksum = computeSha256(cachedFile)
            if (localChecksum != serverChecksum) {
                logger.w(TAG, "Checksum mismatch for ${media.name}: local=$localChecksum server=$serverChecksum")
                cachedFile.delete()
                integrityStats.checksumFailures++
                return false
            }
        }
        
        // Touch the file to update last modified time for LRU
        cachedFile.setLastModified(System.currentTimeMillis())
        return true
    }

    private fun downloadMedia(media: PlaylistMediaItem): DownloadResult {
        val downloadUrl = BuildConfig.BASE_URL.trimEnd('/') + "/" + media.downloadUrl.trimStart('/')

        val request = Request.Builder()
            .url(downloadUrl)
            .build()

        return try {
            // Check available storage first
            val freeSpace = mediaDir.usableSpace
            if (freeSpace < media.size * 2L) {
                return DownloadResult.Error("Insufficient storage: ${freeSpace / (1024 * 1024)}MB free, need ~${(media.size * 2) / (1024 * 1024)}MB")
            }

            val response = downloadClient.newCall(request).execute()
            if (!response.isSuccessful) {
                return DownloadResult.Error("HTTP ${response.code}: ${response.message}")
            }

            val body = response.body ?: return DownloadResult.Error("Empty response body")
            val contentLength = body.contentLength()
            val extension = inferExtension(media)
            val targetFile = File(mediaDir, "${media.mediaId}_${System.currentTimeMillis()}$extension")
            val tempFile = File(mediaDir, "${media.mediaId}.tmp")

            try {
                FileOutputStream(tempFile).use { output ->
                    val buffer = ByteArray(8192)
                    var bytesRead: Long = 0
                    var read: Int

                    body.byteStream().use { input ->
                        while (input.read(buffer).also { read = it } != -1) {
                            output.write(buffer, 0, read)
                            bytesRead += read
                            if (contentLength > 0) {
                                val progress = ((bytesRead * 100) / contentLength).toInt()
                                notifyStatus(-1, -1, media.name, progress, null)
                            }
                        }
                    }
                    output.flush()
                }

                // Validate downloaded file
                if (tempFile.length() == 0L) {
                    tempFile.delete()
                    return DownloadResult.Error("Downloaded file is empty")
                }

                // Validate checksum if available
                val serverChecksum = media.checksum
                if (!serverChecksum.isNullOrEmpty()) {
                    val localChecksum = computeSha256(tempFile)
                    if (localChecksum != serverChecksum) {
                        logger.w(TAG, "Post-download checksum mismatch for ${media.name}: local=$localChecksum server=$serverChecksum")
                        tempFile.delete()
                        return DownloadResult.Error("Checksum verification failed after download")
                    }
                }

                // Remove any old cached version of this media
                mediaDir.listFiles { _, name -> name.startsWith("${media.mediaId}_") }?.forEach { it.delete() }

                // Move temp to final
                tempFile.renameTo(targetFile)
                DownloadResult.Success(targetFile)

            } catch (e: Exception) {
                tempFile.delete()
                integrityStats.failedDownloadCount++
                throw e
            }

        } catch (e: Exception) {
            integrityStats.failedDownloadCount++
            DownloadResult.Error("Download exception: ${e.message}")
        }
    }

    private fun deleteObsoleteMedia(requiredMediaIds: Set<String>) {
        val files = mediaDir.listFiles() ?: return
        for (file in files) {
            if (file.name.endsWith(".tmp")) {
                logger.w(TAG, "Deleting stale temp file: ${file.name}")
                file.delete()
                integrityStats.orphanedFiles++
                continue
            }
            val mediaId = file.name.substringBefore("_")
            if (mediaId !in requiredMediaIds) {
                logger.i(TAG, "Deleted obsolete cached file: ${file.name} (mediaId=$mediaId)")
                file.delete()
            }
        }
    }
    
    private fun enforceCacheLimits(requiredMediaIds: Set<String>) {
        val files = mediaDir.listFiles()?.filter { it.isFile && !it.name.endsWith(".tmp") } ?: return
        val currentSize = files.sumOf { it.length() }
        
        if (currentSize <= SOFT_CACHE_SIZE_BYTES) return
        
        logger.w(TAG, "Cache size (${currentSize / (1024 * 1024)}MB) exceeds soft limit, beginning LRU cleanup")
        
        // Sort files by oldest accessed (lastModified)
        val sortedFiles = files.sortedBy { it.lastModified() }
        var bytesFreed = 0L
        
        for (file in sortedFiles) {
            val mediaId = file.name.substringBefore("_")
            // Never delete media that is currently in the active playlist
            if (mediaId in requiredMediaIds) continue
            
            val size = file.length()
            if (file.delete()) {
                bytesFreed += size
                logger.i(TAG, "LRU cleanup deleted: ${file.name}")
            }
            
            if (currentSize - bytesFreed <= SOFT_CACHE_SIZE_BYTES) {
                break
            }
        }
    }

    private fun computeSha256(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { input ->
            val buffer = ByteArray(8192)
            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                digest.update(buffer, 0, read)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    private fun inferExtension(media: PlaylistMediaItem): String {
        return when (media.type.lowercase()) {
            "video" -> ".mp4"
            "image" -> ".jpg"
            else -> ""
        }
    }

    private fun notifyStatus(totalItems: Int, cachedItems: Int, currentDownload: String?, progress: Int, error: String?) {
        val status = CacheStatus(
            totalItems = totalItems,
            cachedItems = cachedItems,
            currentDownload = currentDownload,
            downloadProgress = progress,
            lastError = error
        )
        listener?.onCacheStatusChanged(status)
    }

    private sealed class DownloadResult {
        data class Success(val file: File) : DownloadResult()
        data class Error(val message: String) : DownloadResult()
    }
}
