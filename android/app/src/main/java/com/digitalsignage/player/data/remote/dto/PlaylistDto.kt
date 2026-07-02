package com.digitalsignage.player.data.remote.dto

import com.squareup.moshi.Json

data class PlaylistSyncResponse(
    @Json(name = "playlistId") val playlistId: String,
    @Json(name = "version") val version: Int,
    @Json(name = "items") val items: List<MediaItemDto>
)

data class MediaItemDto(
    @Json(name = "mediaId") val mediaId: String,
    @Json(name = "url") val url: String,
    @Json(name = "durationMs") val durationMs: Long,
    @Json(name = "order") val order: Int,
    @Json(name = "md5Hash") val md5Hash: String?,
    @Json(name = "sha256Hash") val sha256Hash: String? = null,
    @Json(name = "mediaType") val mediaType: String? = "video"
)


