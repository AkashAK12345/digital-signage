package com.digitalsignage.player.repository

import com.digitalsignage.player.network.ApiService
import com.digitalsignage.player.network.model.DeviceRegisterRequest
import com.digitalsignage.player.storage.DeviceDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String, val exception: Exception? = null) : Result<Nothing>()
}

class DeviceRepository(
    private val apiService: ApiService,
    private val dataStore: DeviceDataStore
) {
    suspend fun registerDevice(androidId: String, name: String, resolution: String, appVersion: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val request = DeviceRegisterRequest(
                    name = name,
                    resolution = resolution,
                    ipAddress = null,
                    appVersion = appVersion,
                    androidId = androidId
                )
                
                val response = apiService.registerDevice(request)
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    dataStore.saveDeviceCredentials(
                        deviceId = body.deviceId,
                        token = body.deviceToken,
                        heartbeat = body.heartbeatInterval,
                        sync = body.syncInterval
                    )
                    Result.Success(body.deviceId)
                } else {
                    Result.Error("Registration failed: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error("Network error: ${e.message}", e)
            }
        }
    }
}
