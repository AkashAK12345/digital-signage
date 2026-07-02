package com.digitalsignage.player.data.remote

import com.digitalsignage.player.data.remote.dto.PlaylistSyncResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("api/v1/device/playlist")
    suspend fun getPlaylist(
        @Header("If-None-Match") currentVersion: String? = null
    ): Response<PlaylistSyncResponse>
    @retrofit2.http.POST("api/v1/device/heartbeat")
    suspend fun postHeartbeat(@retrofit2.http.Body payload: com.digitalsignage.player.data.remote.dto.HeartbeatPayload): retrofit2.Response<Unit>
}

