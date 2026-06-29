package com.digitalsignage.player.network

import com.digitalsignage.player.network.model.CurrentPlaylistResponse
import com.digitalsignage.player.network.model.DeviceRegisterRequest
import com.digitalsignage.player.network.model.DeviceRegisterResponse
import com.digitalsignage.player.network.model.HeartbeatRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("devices/register")
    suspend fun registerDevice(@Body request: DeviceRegisterRequest): Response<DeviceRegisterResponse>

    @GET("devices/{deviceId}/current-playlist")
    suspend fun getCurrentPlaylist(@Path("deviceId") deviceId: String): Response<CurrentPlaylistResponse>

    @POST("devices/heartbeat")
    suspend fun sendHeartbeat(@Body request: HeartbeatRequest): Response<Unit>
}

