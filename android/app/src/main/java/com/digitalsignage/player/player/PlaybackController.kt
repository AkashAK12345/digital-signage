package com.digitalsignage.player.player

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.media3.ui.PlayerView
import com.digitalsignage.player.DigitalSignageApplication
import com.digitalsignage.player.network.model.CurrentPlaylistResponse
import com.digitalsignage.player.network.model.PlaylistItemWithMedia
import com.digitalsignage.player.repository.PlaylistRepository
import com.digitalsignage.player.repository.PlaylistResult
import com.digitalsignage.player.storage.CacheStatus
import com.digitalsignage.player.storage.DeviceDataStore
import com.digitalsignage.player.storage.MediaCacheListener
import com.digitalsignage.player.storage.MediaCacheManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- UI States ---

sealed class PlaybackState {
    object Loading : PlaybackState()
    object Idle : PlaybackState()
    data class Debug(
        val playlist: CurrentPlaylistResponse,
        val connectionStatus: String,
        val lastSyncTime: String,
        val cachedCount: Int
    ) : PlaybackState()
    data class Playing(
        val mediaName: String,
        val index: Int,
        val total: Int
    ) : PlaybackState()
}

// --- Listener for PlaybackActivity ---

interface PlaybackControllerListener {
    fun onStateChanged(state: PlaybackState)
    fun onCacheStatusChanged(status: CacheStatus)
    fun onPlaybackActive(active: Boolean)
}

// --- Controller ---

class PlaybackController(
    private val context: Context,
    private val deviceId: String,
    private val playlistRepository: PlaylistRepository,
    private val dataStore: DeviceDataStore,
    private val mediaCacheManager: MediaCacheManager,
    private val listener: PlaybackControllerListener
) : MediaCacheListener, PlaybackEngineListener {

    companion object {
        private const val TAG = "PlaybackController"
        private const val DEFAULT_SYNC_INTERVAL_SECONDS = 60
    }

    private var syncIntervalSeconds: Int = DEFAULT_SYNC_INTERVAL_SECONDS
    private var heartbeatIntervalSeconds: Int = 60
    private var syncJob: Job? = null
    private var eventBusJob: Job? = null
    private var isActive: Boolean = false
    private var currentPlaylistVersion: Int = -1
    private var currentPlaylist: CurrentPlaylistResponse? = null
    private var currentSession: PlaybackSession? = null

    private var connectionStatus: String = "Connecting..."
    private var lastSyncTime: String = "Never"

    private val syncHandler = Handler(Looper.getMainLooper())
    private val syncRunnable = Runnable { fetchPlaylist() }
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    private var playbackEngine: PlaybackEngine? = null
    private val statistics = PlaybackStatistics()
    
    // Heartbeat State
    private var heartbeatSequence: Long = 0

    // Lifecycle & Health
    val deviceHealthMonitor = DeviceHealthMonitor(context, mediaCacheManager)
    private val heartbeatScheduler = HeartbeatScheduler(context)
    private val lifecycleManager = AppLifecycleManager(context, heartbeatScheduler)

    init {
        mediaCacheManager.setListener(this)
        lifecycleManager.registerController(this)
        
        // Expose to Application for Worker access
        DigitalSignageApplication.playbackController = this
    }

    /**
     * Attaches the views required by the renderers.
     * Must be called before start().
     */
    fun attachViews(imageView: ImageView, playerView: PlayerView) {
        val imageRenderer = ImageRenderer(imageView)
        val videoAdapter = VideoPlayerAdapter(context, playerView)
        val videoRenderer = VideoRenderer(playerView, videoAdapter)

        val renderers = mapOf(
            PlayableMedia.Image::class.java to imageRenderer,
            PlayableMedia.Video::class.java to videoRenderer
        )
        
        playbackEngine = PlaybackEngine(renderers, this)
    }

    fun start() {
        if (isActive) return
        isActive = true
        
        eventBusJob = coroutineScope.launch {
            DeviceEventBus.events.collect { event ->
                when (event) {
                    is DeviceEvent.ConnectivityRestored -> {
                        DigitalSignageApplication.logger.i(TAG, "Forced playlist sync due to connectivity restored")
                        fetchPlaylist()
                    }
                    else -> {}
                }
            }
        }
        
        DigitalSignageApplication.logger.d(TAG, "Controller started")
        listener.onStateChanged(PlaybackState.Loading)
        loadIntervalsAndFetch()
    }

    fun stop() {
        isActive = false
        eventBusJob?.cancel()
        syncHandler.removeCallbacks(syncRunnable)
        playbackEngine?.stop()
        heartbeatScheduler.cancelHeartbeats()
        DigitalSignageApplication.logger.d(TAG, "Controller stopped")
    }

    fun destroy() {
        stop()
        coroutineScope.cancel("PlaybackController destroyed")
        mediaCacheManager.setListener(null)
        playbackEngine?.release()
        playbackEngine = null
        DigitalSignageApplication.connectivityObserver.shutdown()
        DigitalSignageApplication.crashRecoveryManager.recordGracefulShutdown()
    }

    private fun pushStateToRepository() {
        val playerStateStr = when (val state = listener as? PlaybackState ?: PlaybackState.Idle) { // Wait, listener isn't the state, but we can derive it from isActive and currentSession
            // actually let's use isActive
            else -> if (isActive && currentSession != null) "PLAYING" else "IDLE"
        }
        
        val runtimeSnapshot = RuntimeSnapshot(
            playerState = playerStateStr,
            currentPlaylistId = currentPlaylist?.playlistId,
            currentPlaylistVersion = currentPlaylistVersion,
            currentMediaId = statistics.currentMediaId,
            completedItems = statistics.completedItems,
            skippedItems = statistics.skippedItems,
            playbackErrors = statistics.playbackErrors,
            sessionId = statistics.sessionId,
            averagePlaybackLatency = statistics.averagePlaybackLatency,
            lastMediaCompletedAt = statistics.lastMediaCompletedAt,
            lastHeartbeatAt = statistics.lastHeartbeatAt,
            averageImageLoadTime = statistics.averageImageLoadTime,
            averageVideoStartupTime = statistics.averageVideoStartupTime,
            lastPlaylistSyncTime = statistics.lastPlaylistSyncTime,
            heartbeatSuccessRate = statistics.heartbeatSuccessRate,
            consecutivePlaybackFailures = statistics.consecutivePlaybackFailures,
            lastSuccessfulPlayback = statistics.lastSuccessfulPlayback
        )
        
        DigitalSignageApplication.deviceStateRepository.updateRuntimeSnapshot(runtimeSnapshot)
        DigitalSignageApplication.deviceStateRepository.updateHealthSnapshot(deviceHealthMonitor.getHealthSnapshot())
        DigitalSignageApplication.deviceStateRepository.updateDeviceCapabilities(deviceHealthMonitor.getCapabilities())
        DigitalSignageApplication.logger.d(TAG, "RuntimeSnapshot updated in repository")
        DigitalSignageApplication.logger.d(TAG, "HealthSnapshot updated in repository")
    }

    // --- MediaCacheListener ---

    override fun onCacheStatusChanged(status: CacheStatus) {
        listener.onCacheStatusChanged(status)
    }

    // --- PlaybackEngineListener ---

    override fun onPlaybackStarted(session: PlaybackSession) {
        DigitalSignageApplication.logger.i(TAG, "Playback started: '${session.playlistName}' (${session.itemCount} items)")
        
        statistics.currentPlaylistId = session.playlistId
        statistics.playbackStartTime = System.currentTimeMillis()
        statistics.sessionId = java.util.UUID.randomUUID().toString()
        
        listener.onPlaybackActive(true)
        DeviceEventBus.publish(DeviceEvent.PlaybackStarted(session.playlistId))
        pushStateToRepository()
    }

    override fun onMediaDisplayed(item: PlayableMedia, index: Int, total: Int) {
        statistics.currentMediaId = item.mediaId
        
        listener.onStateChanged(PlaybackState.Playing(
            mediaName = item.name,
            index = index,
            total = total
        ))
        pushStateToRepository()
    }

    override fun onMediaCompleted(item: PlayableMedia) {
        statistics.completedItems++
        statistics.lastMediaCompletedAt = System.currentTimeMillis()
        statistics.lastSuccessfulPlayback = System.currentTimeMillis()
        statistics.consecutivePlaybackFailures = 0
        DeviceEventBus.publish(DeviceEvent.PlaybackCompleted(item.mediaId))
        pushStateToRepository()
    }

    override fun onPlaybackError(item: PlayableMedia, message: String) {
        DigitalSignageApplication.logger.e(TAG, "Playback error for '${item.name}': $message")
        statistics.playbackErrors++
        statistics.skippedItems++
        statistics.consecutivePlaybackFailures++
        pushStateToRepository()
    }

    override fun onSessionReloaded(oldVersion: Int, newVersion: Int) {
        DigitalSignageApplication.logger.i(TAG, "Session reloaded: v$oldVersion -> v$newVersion")
        currentPlaylistVersion = newVersion
        statistics.lastPlaylistSyncTime = System.currentTimeMillis()
        DeviceEventBus.publish(DeviceEvent.PlaylistChanged(newVersion))
        pushStateToRepository()
    }

    // --- Sync Logic ---

    private fun loadIntervalsAndFetch() {
        coroutineScope.launch {
            syncIntervalSeconds = dataStore.getSyncInterval()
            heartbeatIntervalSeconds = dataStore.getHeartbeatInterval()
            DigitalSignageApplication.logger.i(TAG, "Sync interval: ${syncIntervalSeconds}s, Heartbeat interval: ${heartbeatIntervalSeconds}s")
            
            // Start heartbeat schedule
            lifecycleManager.startHeartbeatSchedule(deviceId, heartbeatIntervalSeconds.toLong())
            
            fetchPlaylist()
        }
    }

    private fun fetchPlaylist() {
        if (!isActive) return

        DigitalSignageApplication.logger.i(TAG, "Playlist sync started for device $deviceId")

        coroutineScope.launch {
            val result = playlistRepository.getCurrentPlaylist(deviceId)

            when (result) {
                is PlaylistResult.Available -> {
                    connectionStatus = "Connected"
                    lastSyncTime = timeFormat.format(Date())
                    val playlist = result.playlist
                    DigitalSignageApplication.logger.i(
                        TAG,
                        "Playlist received: '${playlist.playlistName}' (${playlist.items.size} items, version ${playlist.playlistVersion})"
                    )

                    if (playlist.playlistVersion != currentPlaylistVersion) {
                        currentPlaylistVersion = playlist.playlistVersion
                        currentPlaylist = playlist
                        DigitalSignageApplication.logger.i(TAG, "Playlist version changed, syncing media")

                        notifyDebugState()
                        syncMediaAndStartPlayback(playlist)
                    } else {
                        DigitalSignageApplication.logger.d(TAG, "Playlist version unchanged, skipping update")
                        notifyDebugState()
                    }

                    scheduleNextSync()
                }

                is PlaylistResult.NoContent -> {
                    connectionStatus = "Connected"
                    lastSyncTime = timeFormat.format(Date())
                    DigitalSignageApplication.logger.i(TAG, "No playlist assigned to this device (HTTP 204)")
                    currentPlaylistVersion = -1
                    currentPlaylist = null
                    currentSession = null
                    playbackEngine?.stop()
                    listener.onPlaybackActive(false)

                    listener.onStateChanged(PlaybackState.Idle)
                    scheduleNextSync()
                }

                is PlaylistResult.Error -> {
                    connectionStatus = "Disconnected"
                    DigitalSignageApplication.logger.e(TAG, "Playlist fetch error: ${result.message}", result.exception)

                    if (currentPlaylist == null) {
                        listener.onStateChanged(PlaybackState.Idle)
                    } else {
                        notifyDebugState()
                    }
                    scheduleNextSync()
                }
            }
        }
    }

    private suspend fun syncMediaAndStartPlayback(playlist: CurrentPlaylistResponse) {
        DigitalSignageApplication.logger.i(TAG, "Starting media download sync for ${playlist.items.size} items")
        val success = mediaCacheManager.syncMedia(playlist)
        if (success) {
            DigitalSignageApplication.logger.i(TAG, "All media cached successfully")
        } else {
            DigitalSignageApplication.logger.w(TAG, "Media sync completed with errors")
        }
        notifyDebugState()

        // Build PlaybackSession from cached media
        val session = buildSession(playlist)
        if (session != null && session.hasItems) {
            currentSession = session
            playbackEngine?.startSession(session)
        } else {
            DigitalSignageApplication.logger.w(TAG, "No playable media found after sync")
            listener.onPlaybackActive(false)
            listener.onStateChanged(PlaybackState.Idle)
        }
    }

    private fun buildSession(playlist: CurrentPlaylistResponse): PlaybackSession? {
        val playableItems = playlist.items
            .sortedBy { it.order }
            .mapNotNull { item -> convertToPlayableMedia(item) }

        if (playableItems.isEmpty()) return null

        DigitalSignageApplication.logger.i(TAG, "Built PlaybackSession: ${playableItems.size} playable items from ${playlist.items.size} total")

        return PlaybackSession(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistVersion = playlist.playlistVersion,
            items = playableItems,
            currentIndex = 0
        )
    }

    private fun convertToPlayableMedia(item: PlaylistItemWithMedia): PlayableMedia? {
        val cachedFile = mediaCacheManager.getCachedFilePath(item.media.mediaId)
        if (cachedFile == null || !cachedFile.exists()) {
            DigitalSignageApplication.logger.w(TAG, "No cached file for '${item.media.name}' (${item.media.mediaId}), skipping")
            return null
        }

        return when (item.media.type.lowercase()) {
            "image" -> PlayableMedia.Image(
                mediaId = item.media.mediaId,
                name = item.media.name,
                durationSeconds = item.duration,
                localFile = cachedFile,
                order = item.order
            )
            "video" -> PlayableMedia.Video(
                mediaId = item.media.mediaId,
                name = item.media.name,
                durationSeconds = item.duration,
                localFile = cachedFile,
                order = item.order
            )
            else -> {
                DigitalSignageApplication.logger.w(TAG, "Unknown media type '${item.media.type}' for '${item.media.name}', skipping")
                null
            }
        }
    }

    private fun scheduleNextSync() {
        if (!isActive) return
        val delayMs = syncIntervalSeconds * 1000L
        syncHandler.removeCallbacks(syncRunnable)
        syncHandler.postDelayed(syncRunnable, delayMs)
        DigitalSignageApplication.logger.d(TAG, "Next sync scheduled in ${syncIntervalSeconds}s")
    }

    private fun notifyDebugState() {
        currentPlaylist?.let { playlist ->
            val cachedCount = mediaCacheManager.getCachedFileCount()
            listener.onStateChanged(PlaybackState.Debug(
                playlist = playlist,
                connectionStatus = connectionStatus,
                lastSyncTime = lastSyncTime,
                cachedCount = cachedCount
            ))
        }
    }
}

