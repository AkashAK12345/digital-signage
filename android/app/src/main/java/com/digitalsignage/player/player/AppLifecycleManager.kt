package com.digitalsignage.player.player

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.digitalsignage.player.DigitalSignageApplication

/**
 * Monitors application foreground/background transitions.
 * Coordinates with PlaybackController to pause/resume activities
 * and schedules heartbeat execution upon app start.
 */
class AppLifecycleManager(
    private val context: Context,
    private val heartbeatScheduler: HeartbeatScheduler
) : DefaultLifecycleObserver {

    companion object {
        private const val TAG = "AppLifecycleManager"
    }

    private val logger = DigitalSignageApplication.logger
    private var playbackController: PlaybackController? = null

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun registerController(controller: PlaybackController) {
        this.playbackController = controller
    }

    fun startHeartbeatSchedule(deviceId: String, intervalSeconds: Long) {
        heartbeatScheduler.schedulePeriodicHeartbeat(deviceId, intervalSeconds)
    }

    override fun onStart(owner: LifecycleOwner) {
        logger.i(TAG, "Application moved to Foreground")
        // In the future: resume playback if it was paused
    }

    override fun onStop(owner: LifecycleOwner) {
        logger.i(TAG, "Application moved to Background")
        // In the future: pause playback to save resources
    }
}
