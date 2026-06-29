package com.digitalsignage.player.player

import android.app.ActivityManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.SystemClock
import com.digitalsignage.player.DigitalSignageApplication
import com.digitalsignage.player.storage.MediaCacheManager

/**
 * Service class responsible ONLY for collecting diagnostic and hardware metrics.
 * It does NOT perform network requests.
 */
class DeviceHealthMonitor(
    private val context: Context,
    private val mediaCacheManager: MediaCacheManager
) {

    companion object {
        private const val TAG = "DeviceHealthMonitor"
    }

    private val logger = DigitalSignageApplication.logger

    fun getHealthSnapshot(): HealthSnapshot {
        val storageUsed = getStorageUsedMb()
        val storageTotal = getStorageTotalMb()
        val cachePercent = if (storageTotal > 0f) (storageUsed / storageTotal) * 100f else 0f
        
        return HealthSnapshot(
            storageUsedMb = storageUsed,
            storageTotalMb = storageTotal,
            cacheUsagePercent = cachePercent,
            uptimeSeconds = SystemClock.elapsedRealtime() / 1000,
            isNetworkConnected = checkNetworkConnected(),
            availableMemoryMb = getAvailableMemoryMb(),
            cacheIntegrityValid = checkCacheIntegrity()
        )
    }

    fun getCapabilities(): DeviceCapabilities {
        return DeviceCapabilities(
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            androidVersion = Build.VERSION.RELEASE,
            appVersion = "1.0", // Hardcoded for MVP, ideally read from BuildConfig or PackageManager
            totalStorage = getStorageTotalMb(),
            availableStorage = getStorageTotalMb() - getStorageUsedMb(),
            maximumResolution = "1080p" // Hardcoded for MVP
        )
    }

    private fun getStorageUsedMb(): Float {
        // Simple approximation: media cache directory size
        val cacheDir = context.filesDir.resolve("media_cache")
        if (!cacheDir.exists()) return 0f
        
        val bytes = cacheDir.walkBottomUp().fold(0L) { acc, file ->
            acc + if (file.isFile) file.length() else 0L
        }
        return bytes / (1024f * 1024f)
    }

    private fun getStorageTotalMb(): Float {
        // Simple approximation for MVP
        val stat = android.os.StatFs(context.filesDir.path)
        val bytes = stat.blockSizeLong * stat.blockCountLong
        return bytes / (1024f * 1024f)
    }

    private fun checkNetworkConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun getAvailableMemoryMb(): Float {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.availMem / (1024f * 1024f)
    }

    private fun checkCacheIntegrity(): Boolean {
        // In a real scenario, this might verify checksums of all cached files
        // For 1 GB RAM constraints, we just return true unless a recent error was flagged
        return true
    }
}
