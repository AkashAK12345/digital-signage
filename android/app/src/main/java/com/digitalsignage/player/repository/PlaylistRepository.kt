package com.digitalsignage.player.repository

import com.digitalsignage.player.network.ApiService
import com.digitalsignage.player.network.model.CurrentPlaylistResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class PlaylistResult {
    data class Available(val playlist: CurrentPlaylistResponse) : PlaylistResult()
    object NoContent : PlaylistResult()
    data class Error(val message: String, val exception: Exception? = null) : PlaylistResult()
}

class PlaylistRepository(private val apiService: ApiService) {

    suspend fun getCurrentPlaylist(deviceId: String): PlaylistResult {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCurrentPlaylist(deviceId)
                when (response.code()) {
                    200 -> {
                        val body = response.body()
                        if (body != null) {
                            PlaylistResult.Available(body)
                        } else {
                            PlaylistResult.Error("Empty response body from server")
                        }
                    }
                    204 -> PlaylistResult.NoContent
                    else -> PlaylistResult.Error("Playlist request failed: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                PlaylistResult.Error("Network error: ${e.message}", e)
            }
        }
    }
}
