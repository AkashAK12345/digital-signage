package com.digitalsignage.player.data.remote.dto

import com.squareup.moshi.Json

data class HeartbeatPayload(
    @Json(name = "installationId") val installationId: String,
    @Json(name = "currentPlaylistId") val currentPlaylistId: String?,
    @Json(name = "currentMediaId") val currentMediaId: String?,
    @Json(name = "currentPositionMs") val currentPositionMs: Long,
    @Json(name = "playbackState") val playbackState: String,
    @Json(name = "appVersionName") val appVersionName: String,
    @Json(name = "appVersionCode") val appVersionCode: Int,
    @Json(name = "androidVersion") val androidVersion: String,
    @Json(name = "uptimeSeconds") val uptimeSeconds: Long,
    @Json(name = "manufacturer") val manufacturer: String,
    @Json(name = "model") val model: String,
    @Json(name = "availableStorageBytes") val availableStorageBytes: Long,
    @Json(name = "totalStorageBytes") val totalStorageBytes: Long,
    @Json(name = "availableMemoryBytes") val availableMemoryBytes: Long,
    @Json(name = "totalMemoryBytes") val totalMemoryBytes: Long,
    @Json(name = "networkType") val networkType: String
)

