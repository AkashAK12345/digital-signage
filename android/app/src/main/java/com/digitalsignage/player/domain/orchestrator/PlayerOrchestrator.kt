package com.digitalsignage.player.domain.orchestrator

import com.digitalsignage.player.core.event.PlayerEvent
import com.digitalsignage.player.core.event.PlayerEventBus
import com.digitalsignage.player.core.logging.Logger
import com.digitalsignage.player.core.network.NetworkMonitor
import com.digitalsignage.player.data.repository.DeviceRepositoryImpl
import com.digitalsignage.player.domain.repository.PlaylistRepository
import com.digitalsignage.player.domain.state.PlayerState
import com.digitalsignage.player.domain.state.PlayerStateMachine
import com.digitalsignage.player.core.error.AppError
import com.digitalsignage.player.domain.repository.Result
import com.digitalsignage.player.core.kiosk.KioskManager
import com.digitalsignage.player.core.kiosk.MaintenanceSessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import android.app.Activity

interface PlayerOrchestrator {
    fun initialize()
    fun attachActivity(activity: Activity)
    fun detachActivity()
    fun onUserInteraction()
    fun requestMaintenance()
    fun onMaintenanceAuthorized()
}

@Singleton
class PlayerOrchestratorImpl @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: android.content.Context,
    private val stateMachine: PlayerStateMachine,
    private val eventBus: PlayerEventBus,
    private val logger: Logger,
    private val deviceRepository: DeviceRepositoryImpl,
    private val playlistRepository: PlaylistRepository,
    private val networkMonitor: NetworkMonitor,
    private val downloadManager: com.digitalsignage.player.workers.download.DownloadManager,
    private val playlistExecutor: com.digitalsignage.player.domain.playback.PlaylistExecutor,
    private val heartbeatManager: com.digitalsignage.player.workers.heartbeat.HeartbeatManager,
    private val startupValidator: com.digitalsignage.player.core.recovery.StartupValidator,
    private val crashRecoveryManager: com.digitalsignage.player.core.recovery.CrashRecoveryManager,
    private val kioskManager: KioskManager,
    private val maintenanceSessionManager: MaintenanceSessionManager
) : PlayerOrchestrator {

    private val scope = CoroutineScope(Dispatchers.Main)
    private var syncJob: Job? = null
    private var currentActivity: Activity? = null
    
    override fun initialize() {
        crashRecoveryManager.initialize()
        
        scope.launch {
            startupValidator.validateAndRecover()
            logger.i("Orchestrator", "Startup validation completed")
            observePlaylistChanges()
            observeEvents()
        }
    }

    private fun observeEvents() {
        scope.launch {
            eventBus.events.collect { event ->
                when (event) {
                    is PlayerEvent.MaintenanceStarted -> {
                        if (currentActivity != null) {
                            kioskManager.enterMaintenanceMode(currentActivity!!)
                        }
                    }
                    is PlayerEvent.MaintenanceEnded -> {
                        if (currentActivity != null) {
                            kioskManager.exitMaintenanceMode(currentActivity!!)
                        }
                    }
                    is PlayerEvent.SplashCompleted -> {
                        scope.launch {
                            if (deviceRepository.validateLocalCredentials()) {
                                stateMachine.transitionTo(PlayerState.SYNCING)
                                executeCommand(PlayerCommand.SyncPlaylist)
                                heartbeatManager.start()
                            } else {
                                stateMachine.transitionTo(PlayerState.REGISTERING)
                                executeCommand(PlayerCommand.RegisterDevice)
                            }
                        }
                    }
                    is PlayerEvent.RegistrationSucceeded -> {
                        stateMachine.transitionTo(PlayerState.SYNCING)
                        executeCommand(PlayerCommand.SyncPlaylist)
                        heartbeatManager.start()
                    }
                    is PlayerEvent.PlaylistUpdated -> {
                        downloadManager.startProcessing()
                        stateMachine.transitionTo(PlayerState.DOWNLOADING)
                        executeCommand(PlayerCommand.DownloadMedia)
                    }
                    is PlayerEvent.PlaylistReady -> {
                        stateMachine.transitionTo(PlayerState.READY)
                        if (stateMachine.targetState.value == PlayerState.PLAYING) {
                            stateMachine.transitionTo(PlayerState.PLAYING)
                            executeCommand(PlayerCommand.StartPlayback)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun observePlaylistChanges() {
        scope.launch {
            playlistRepository.observeCurrentPlaylist().collect { playlist ->
                if (playlist != null && stateMachine.currentState.value == PlayerState.PLAYING) {
                    playlistExecutor.execute(playlist)
                }
            }
        }
    }

    private fun executeCommand(command: PlayerCommand) {
        when (command) {
            is PlayerCommand.RegisterDevice -> {
                // Implementation in DeviceRepository
            }
            is PlayerCommand.SyncPlaylist -> {
                syncJob?.cancel()
                syncJob = scope.launch {
                    attemptSync()
                }
            }
            is PlayerCommand.StartPlayback -> {
                logger.i("Orchestrator", "Executing StartPlayback")
                scope.launch {
                    if (currentActivity != null) {
                        kioskManager.enableKiosk(currentActivity!!)
                        eventBus.publish(PlayerEvent.KioskStateChanged(kioskManager.isKioskActive()))
                    }
                    playlistRepository.observeCurrentPlaylist().first { it != null }?.let { activePlaylist ->
                        playlistExecutor.execute(activePlaylist)
                    }
                }
            }
            else -> {}
        }
    }
    
    private suspend fun attemptSync() {
        if (networkMonitor.isOnline.first() == false) {
            logger.w("Orchestrator", "Offline. Sync paused.")
            return
        }
        
        when (val result = playlistRepository.syncPlaylist()) {
            is Result.Success -> {
                val wasUpdated = result.data
                if (wasUpdated) {
                    eventBus.publish(PlayerEvent.PlaylistUpdated)
                    downloadManager.startProcessing()
                } else {
                    // Not modified, ready for playback
                    stateMachine.transitionTo(PlayerState.READY)
                    if (stateMachine.targetState.value == PlayerState.PLAYING) {
                        eventBus.publish(PlayerEvent.PlaylistUpdated)
                    }
                }
            }
            is Result.Error -> {
                if (result.exception is AppError.Retryable) {
                    delay(5000)
                    attemptSync() // Basic retry for sync
                } else {
                    stateMachine.transitionToError(result.exception as AppError)
                }
            }
        }
    }

    override fun attachActivity(activity: Activity) {
        currentActivity = activity
        if (stateMachine.currentState.value == PlayerState.READY || stateMachine.currentState.value == PlayerState.PLAYING) {
            kioskManager.enableKiosk(activity)
            eventBus.publish(PlayerEvent.KioskStateChanged(kioskManager.isKioskActive()))
        }
    }

    override fun detachActivity() {
        if (currentActivity != null) {
            kioskManager.disableKiosk(currentActivity!!)
            currentActivity = null
        }
    }

    override fun onUserInteraction() {
        maintenanceSessionManager.onUserInteraction()
    }

    override fun requestMaintenance() {
        val activity = currentActivity
        if (activity != null) {
            eventBus.publish(PlayerEvent.MaintenanceRequested)
        } else {
            logger.w("PlayerOrchestrator", "Cannot start maintenance: No active UI")
        }
    }

    override fun onMaintenanceAuthorized() {
        maintenanceSessionManager.startSession()
    }
}






