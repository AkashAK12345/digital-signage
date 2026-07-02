package com.digitalsignage.player.ui

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.ui.PlayerView
import com.digitalsignage.player.core.event.PlayerEvent
import com.digitalsignage.player.core.event.PlayerEventBus
import com.digitalsignage.player.databinding.ActivityPlaybackBinding
import com.digitalsignage.player.domain.orchestrator.PlayerOrchestrator
import com.digitalsignage.player.domain.playback.PlaybackEngine
import com.digitalsignage.player.player.playback.ExoPlayerEngineImpl
import com.digitalsignage.player.data.local.datastore.RuntimeConfigStoreImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaybackBinding

    @Inject lateinit var playerOrchestrator: PlayerOrchestrator
    @Inject lateinit var playbackEngine: PlaybackEngine
    @Inject lateinit var eventBus: PlayerEventBus
    @Inject lateinit var runtimeConfigStore: RuntimeConfigStoreImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaybackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val exoEngine = playbackEngine as? ExoPlayerEngineImpl
        if (exoEngine != null) {
            val playerView = binding.playerView
            playerView.player = exoEngine.exoPlayer
        }

        playerOrchestrator.initialize()
        
        lifecycleScope.launch {
            eventBus.events.collectLatest { event ->
                if (event is PlayerEvent.MaintenanceRequested) {
                    showMaintenanceDialog()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        playerOrchestrator.attachActivity(this)
    }

    override fun onStop() {
        super.onStop()
        playerOrchestrator.detachActivity()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.playerView.player = null
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        playerOrchestrator.onUserInteraction()
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            playerOrchestrator.requestMaintenance()
            return true
        }
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            event?.startTracking()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.isTracking == true && !event.isCanceled) {
            // Normal back press, ignore or handle if needed
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    private fun showMaintenanceDialog() {
        MaintenanceDialog(this, runtimeConfigStore) {
            // Success callback - already handled by MaintenanceSessionManager
        }.show()
    }
}

