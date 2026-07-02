package com.digitalsignage.player.data.repository

import com.digitalsignage.player.core.error.AppError
import com.digitalsignage.player.core.logging.Logger
import com.digitalsignage.player.data.local.AppDatabase
import com.digitalsignage.player.data.local.DownloadSessionEntity
import com.digitalsignage.player.data.local.MediaItemEntity
import com.digitalsignage.player.data.local.PlaylistEntity
import com.digitalsignage.player.data.local.datastore.RuntimeConfigStoreImpl
import com.digitalsignage.player.data.remote.ApiService
import com.digitalsignage.player.domain.model.DownloadState
import com.digitalsignage.player.domain.model.MediaItem
import com.digitalsignage.player.domain.model.MediaType
import com.digitalsignage.player.domain.model.Playlist
import com.digitalsignage.player.domain.model.PlaylistState
import com.digitalsignage.player.domain.repository.PlaylistRepository
import com.digitalsignage.player.domain.repository.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import java.io.File

@Singleton
class PlaylistRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val database: AppDatabase,
    private val configStore: RuntimeConfigStoreImpl,
    private val logger: Logger
) : PlaylistRepository {

    override suspend fun syncPlaylist(): Result<Boolean> {
        return try {
            val currentETag = configStore.playlistETag.firstOrNull()
            
            logger.d("PlaylistRepository", "Requesting playlist sync with If-None-Match: ${currentETag}")
            val response = apiService.getPlaylist(currentETag)
            
            if (response.isSuccessful) {
                val syncData = response.body()
                val newETag = response.headers()["ETag"]
                
                if (syncData != null) {
                    logger.i("PlaylistRepository", "New playlist version ${syncData.version} received. Saving as PENDING.")
                    
                    val entity = PlaylistEntity(
                        playlistId = syncData.playlistId,
                        version = syncData.version,
                        state = PlaylistState.PENDING,
                        lastSyncedAt = System.currentTimeMillis()
                    )
                    
                    val activePlaylist = database.playlistDao().getPlaylistByState(PlaylistState.ACTIVE)
                    val activeItems = activePlaylist?.let { database.playlistDao().getMediaItemsForPlaylist(it.playlistId) } ?: emptyList()
                    
                    val itemEntities = syncData.items.map { dto ->
                        val existingItem = activeItems.find { it.mediaId == dto.mediaId }
                        MediaItemEntity(
                            mediaId = dto.mediaId,
                            playlistId = syncData.playlistId,
                            url = dto.url,
                            durationMs = dto.durationMs,
                            displayOrder = dto.order,
                            md5Hash = dto.md5Hash,
                            sha256Hash = dto.sha256Hash,
                            mediaType = dto.mediaType ?: "video",
                            isDownloaded = existingItem?.isDownloaded ?: false,
                            localFilePath = existingItem?.localFilePath
                        )
                    }
                    
                    // Metadata update only in transaction
                    database.playlistDao().insertPendingPlaylistTransaction(entity, itemEntities)
                    
                    // Store ETag independently from version
                    if (newETag != null) {
                        configStore.savePlaylistETag(newETag)
                    }
                    
                    // Queue downloads via persistent storage
                    enqueueDownloads(itemEntities.filter { !it.isDownloaded })
                    
                    Result.Success(true)
                } else {
                    Result.Error(AppError.Recoverable("Empty response body"))
                }
            } else if (response.code() == 304) {
                logger.i("PlaylistRepository", "Playlist not modified. ETag ${currentETag} is up to date.")
                Result.Success(false)
            } else {
                Result.Error(AppError.Retryable("Failed to fetch playlist: ${response.code()}"))
            }
        } catch (e: Exception) {
            logger.e("PlaylistRepository", "Exception during sync", e)
            Result.Error(AppError.Retryable("Network error", e))
        }
    }

    override fun observeCurrentPlaylist(): Flow<Playlist?> {
        return database.playlistDao().observeActivePlaylist().map { entity ->
            if (entity == null) return@map null
            val items = database.playlistDao().getMediaItemsForPlaylist(entity.playlistId).map {
                MediaItem(
                    mediaId = it.mediaId,
                    url = it.url,
                    durationMs = it.durationMs,
                    order = it.displayOrder,
                    md5Hash = it.md5Hash,
                    sha256Hash = it.sha256Hash,
                    mediaType = mapMediaType(it.mediaType),
                    isDownloaded = it.isDownloaded
                )
            }
            Playlist(entity.playlistId, entity.version, entity.state, items)
        }
    }
    
    private suspend fun enqueueDownloads(itemsToDownload: List<MediaItemEntity>) {
        if (itemsToDownload.isNotEmpty()) {
            val tasks = itemsToDownload.map { item ->
                DownloadSessionEntity(
                    mediaId = item.mediaId,
                    url = item.url,
                    downloadState = DownloadState.QUEUED,
                    retryCount = 0,
                    priority = 10,
                    expectedChecksumMd5 = item.md5Hash,
                    expectedChecksumSha256 = item.sha256Hash,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    destinationPath = "cache/media/${item.mediaId}"
                )
            }
            database.downloadSessionDao().insertTasks(tasks)
            logger.logEvent("MediaDownloadQueuedToDB", mapOf("count" to tasks.size))
        }
    }
    
    private fun mapMediaType(typeStr: String): MediaType {
        return try {
            MediaType.valueOf(typeStr.uppercase())
        } catch (e: Exception) {
            MediaType.UNKNOWN
        }
    }
}


