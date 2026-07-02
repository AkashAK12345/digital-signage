package com.digitalsignage.player.domain.model

enum class PlaylistState { ACTIVE, PENDING, ARCHIVED }
enum class MediaType { VIDEO, IMAGE, HTML, AUDIO, UNKNOWN }
enum class DownloadState { QUEUED, DOWNLOADING, PAUSED, COMPLETED, FAILED, CANCELLED }

data class Playlist(
    val playlistId: String,
    val version: Int,
    val state: PlaylistState,
    val items: List<MediaItem>
)

data class MediaItem(
    val mediaId: String,
    val url: String,
    val durationMs: Long,
    val order: Int,
    val md5Hash: String?,
    val sha256Hash: String?,
    val mediaType: MediaType,
    val isDownloaded: Boolean
)


