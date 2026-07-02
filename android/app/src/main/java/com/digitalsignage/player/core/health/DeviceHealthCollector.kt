package com.digitalsignage.player.core.health

import android.app.ActivityManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.SystemClock
import com.digitalsignage.player.BuildConfig
import com.digitalsignage.player.data.local.AppDatabase
import com.digitalsignage.player.domain.model.PlaylistState
import com.digitalsignage.player.domain.playback.PlaybackEngine
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceHealthCollector @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: AppDatabase,
    private val playbackEngine: PlaybackEngine
) {

    suspend fun collectHealthData(installationId: String): com.digitalsignage.player.data.remote.dto.HeartbeatPayload {
        val activePlaylist = database.playlistDao().getPlaylistByState(PlaylistState.ACTIVE)
        val playbackStateStr = if (playbackEngine.isPlaying()) "PLAYING" else "IDLE"

        val stat = StatFs(Environment.getDataDirectory().path)
        val availableBytes = stat.availableBlocksLong * stat.blockSizeLong
        val totalBytes = stat.blockCountLong * stat.blockSizeLong

        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        val networkType = when {
            capabilities == null -> "OFFLINE"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "ETHERNET"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WIFI"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "CELLULAR"
            else -> "UNKNOWN"
        }

        return com.digitalsignage.player.data.remote.dto.HeartbeatPayload(
            installationId = installationId,
            currentPlaylistId = playbackEngine.getCurrentPlaylistId() ?: activePlaylist?.playlistId,
            currentMediaId = playbackEngine.getCurrentMediaId(),
            currentPositionMs = playbackEngine.getCurrentPosition(),
            playbackState = playbackStateStr,
            appVersionName = BuildConfig.VERSION_NAME,
            appVersionCode = BuildConfig.VERSION_CODE,
            androidVersion = Build.VERSION.RELEASE,
            uptimeSeconds = SystemClock.elapsedRealtime() / 1000,
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            availableStorageBytes = availableBytes,
            totalStorageBytes = totalBytes,
            availableMemoryBytes = memoryInfo.availMem,
            totalMemoryBytes = memoryInfo.totalMem,
            networkType = networkType
        )
    }
}
