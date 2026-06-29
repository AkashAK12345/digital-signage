package com.digitalsignage.player.ui

import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import com.digitalsignage.player.DigitalSignageApplication
import com.digitalsignage.player.databinding.ActivityPlaybackBinding
import com.digitalsignage.player.network.RetrofitClient
import com.digitalsignage.player.player.PlaybackController
import com.digitalsignage.player.player.PlaybackControllerListener
import com.digitalsignage.player.player.PlaybackState
import com.digitalsignage.player.repository.PlaylistRepository
import com.digitalsignage.player.storage.CacheStatus
import com.digitalsignage.player.storage.DeviceDataStore
import com.digitalsignage.player.storage.MediaCacheManager

class PlaybackActivity : AppCompatActivity(), PlaybackControllerListener {

    companion object {
        const val EXTRA_DEVICE_ID = "extra_device_id"
        private const val TAG = "Playback"
    }

    private lateinit var binding: ActivityPlaybackBinding
    private lateinit var controller: PlaybackController
    private var deviceId: String = "Unknown"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaybackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()

        deviceId = intent.getStringExtra(EXTRA_DEVICE_ID) ?: run {
            DigitalSignageApplication.logger.e(TAG, "No device ID provided to PlaybackActivity")
            finish()
            return
        }
        
        val startupReasonStr = intent.getStringExtra("startup_reason") ?: "NORMAL"
        logger.i(TAG, "PlaybackActivity starting for device: $deviceId (Reason: $startupReasonStr)")
        val mediaCacheManager = MediaCacheManager(applicationContext)

        val dataStore = DeviceDataStore(applicationContext)
        val playlistRepository = PlaylistRepository(RetrofitClient.apiService)

        controller = PlaybackController(
            context = applicationContext,
            deviceId = deviceId,
            playlistRepository = playlistRepository,
            dataStore = dataStore,
            mediaCacheManager = mediaCacheManager,
            listener = this
        )
        controller.attachViews(binding.imageView, binding.playerView)

        DigitalSignageApplication.logger.i(TAG, "PlaybackActivity created for device $deviceId")
    }

    override fun onStart() {
        super.onStart()
        controller.start()
    }

    override fun onStop() {
        super.onStop()
        controller.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.destroy()
    }

    // --- PlaybackControllerListener ---

    override fun onStateChanged(state: PlaybackState) {
        runOnUiThread {
            when (state) {
                is PlaybackState.Loading -> showLoading()
                is PlaybackState.Idle -> showIdle()
                is PlaybackState.Debug -> showDebugView(state)
                is PlaybackState.Playing -> showPlaying()
            }
        }
    }

    override fun onCacheStatusChanged(status: CacheStatus) {
        runOnUiThread {
            updateDebugDownloadStatus(status)
        }
    }

    override fun onPlaybackActive(active: Boolean) {
        runOnUiThread {
            if (active) {
                // Hide all overlays — media renderers take over their respective views natively
                binding.loadingIndicator.visibility = View.GONE
                binding.idleView.visibility = View.GONE
                binding.debugView.visibility = View.GONE
            }
        }
    }

    // --- View State Helpers ---

    private fun showLoading() {
        binding.loadingIndicator.visibility = View.VISIBLE
        binding.idleView.visibility = View.GONE
        binding.debugView.visibility = View.GONE
        binding.playerView.visibility = View.GONE
        binding.imageView.visibility = View.GONE
    }

    private fun showIdle() {
        binding.loadingIndicator.visibility = View.GONE
        binding.idleView.visibility = View.VISIBLE
        binding.debugView.visibility = View.GONE
        binding.playerView.visibility = View.GONE
        binding.imageView.visibility = View.GONE
    }

    private fun showDebugView(state: PlaybackState.Debug) {
        binding.loadingIndicator.visibility = View.GONE
        binding.idleView.visibility = View.GONE
        binding.debugView.visibility = View.VISIBLE
        binding.playerView.visibility = View.GONE
        binding.imageView.visibility = View.GONE

        binding.tvDebugDeviceId.text = "Device ID: $deviceId"
        binding.tvDebugConnection.text = "Status: ${state.connectionStatus}"
        binding.tvDebugPlaylistName.text = state.playlist.playlistName
        binding.tvDebugPlaylistVersion.text = "Version: ${state.playlist.playlistVersion}"
        binding.tvDebugItemCount.text = "Items: ${state.playlist.items.size}"

        binding.tvDebugCachedCount.text = "Cached: ${state.cachedCount} files"
        binding.tvDebugLastSync.text = "Last Sync: ${state.lastSyncTime}"

        if (binding.tvDebugDownloadStatus.text.isNullOrEmpty() || binding.tvDebugDownloadStatus.text == "Checking cache...") {
            binding.tvDebugDownloadStatus.text = "Checking cache..."
            binding.downloadProgressBar.progress = 0
            binding.tvDebugError.visibility = View.GONE
        }
    }

    private fun showPlaying() {
        // When actively playing, ensure overlays are hidden.
        // Renderers manage imageView/playerView visibility natively.
        binding.loadingIndicator.visibility = View.GONE
        binding.idleView.visibility = View.GONE
        binding.debugView.visibility = View.GONE
    }

    private fun updateDebugDownloadStatus(status: CacheStatus) {
        if (binding.debugView.visibility != View.VISIBLE) return

        if (status.currentDownload != null) {
            binding.tvDebugDownloadStatus.text = "Downloading: ${status.currentDownload}"
            binding.downloadProgressBar.progress = status.downloadProgress
        } else if (status.totalItems > 0) {
            binding.tvDebugDownloadStatus.text = "Downloaded: ${status.cachedItems}/${status.totalItems}"
            binding.downloadProgressBar.progress = if (status.totalItems > 0) {
                (status.cachedItems * 100) / status.totalItems
            } else {
                0
            }
        }

        if (status.totalItems > 0) {
            binding.tvDebugCachedCount.text = "Cached: ${status.cachedItems}/${status.totalItems} files"
        }

        if (status.lastError != null) {
            binding.tvDebugError.visibility = View.VISIBLE
            binding.tvDebugError.text = "Error: ${status.lastError}"
        } else {
            binding.tvDebugError.visibility = View.GONE
        }
    }

    // --- System UI ---

    private fun hideSystemUI() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
            )
        }
    }
}
