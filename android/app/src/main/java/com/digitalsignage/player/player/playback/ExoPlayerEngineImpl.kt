package com.digitalsignage.player.player.playback

import android.content.Context
import android.net.Uri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import com.digitalsignage.player.core.event.PlayerEvent
import com.digitalsignage.player.core.event.PlayerEventBus
import com.digitalsignage.player.core.logging.Logger
import com.digitalsignage.player.domain.model.Playlist
import com.digitalsignage.player.domain.model.MediaType
import com.digitalsignage.player.domain.playback.PlaybackEngine
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.cancel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExoPlayerEngineImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val eventBus: PlayerEventBus,
    private val logger: Logger
) : PlaybackEngine {

    var exoPlayer: ExoPlayer? = null
        private set

    private var currentPlaylistId: String? = null
        
    private val scope = CoroutineScope(Dispatchers.Main)
    private var watchdogJob: Job? = null

    override fun initialize() {
        if (exoPlayer != null) return
        
        // Ensure Preloading using DefaultLoadControl
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
            ).build()
        
        exoPlayer = ExoPlayer.Builder(context)
            .setLoadControl(loadControl)
            .build().apply {
            
            // Ensure display stays awake during playback
            setWakeMode(C.WAKE_MODE_LOCAL)
            repeatMode = Player.REPEAT_MODE_ALL
            
            addListener(object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    if (mediaItem != null) {
                        eventBus.publish(PlayerEvent.PlaybackStarted(mediaItem.mediaId))
                        if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_REPEAT) {
                            eventBus.publish(PlayerEvent.PlaylistLooped)
                        } else if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
                            eventBus.publish(PlayerEvent.MediaItemTransitioned(null, mediaItem.mediaId))
                        }
                    }
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        eventBus.publish(PlayerEvent.PlaylistCompleted)
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    val currentItem = exoPlayer?.currentMediaItem
                    val mediaId = currentItem?.mediaId ?: "unknown"
                    logger.e("ExoPlayerEngine", "Playback error for media ${mediaId}", error)
                    eventBus.publish(PlayerEvent.PlaybackFailed(mediaId, error))
                    
                    recoverPlayback()
                }
                
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    val currentId = exoPlayer?.currentMediaItem?.mediaId ?: return
                    if (isPlaying) {
                        eventBus.publish(PlayerEvent.PlaybackResumed(currentId))
                    } else {
                        if (exoPlayer?.playbackState != Player.STATE_ENDED) {
                            eventBus.publish(PlayerEvent.PlaybackPaused(currentId))
                        }
                    }
                }
            })
        }
        
        startWatchdog()
    }
    
    private fun recoverPlayback() {
        logger.i("ExoPlayerEngine", "Attempting recovery: skipping failed media")
        if (exoPlayer?.hasNextMediaItem() == true) {
            exoPlayer?.seekToNextMediaItem()
            exoPlayer?.prepare()
            exoPlayer?.play()
        } else {
            // If it's the only item, we might need to recreate or restart
            exoPlayer?.seekToDefaultPosition()
            exoPlayer?.prepare()
            exoPlayer?.play()
        }
    }
    
    private fun startWatchdog() {
        watchdogJob?.cancel()
        watchdogJob = scope.launch {
            var lastPosition = -1L
            var stalledCount = 0
            while (isActive) {
                delay(2000)
                val player = exoPlayer ?: continue
                if (player.isPlaying && player.playbackState == Player.STATE_READY) {
                    val currentPosition = player.currentPosition
                    if (currentPosition == lastPosition) {
                        stalledCount++
                        if (stalledCount >= 3) { // 6 seconds stalled
                            logger.e("ExoPlayerEngine", "Watchdog triggered! Playback stalled.")
                            recoverPlayback()
                            stalledCount = 0
                        }
                    } else {
                        stalledCount = 0
                        lastPosition = currentPosition
                    }
                } else {
                    stalledCount = 0
                }
            }
        }
    }

    override fun setPlaylist(playlist: Playlist) {
        currentPlaylistId = playlist.playlistId
        initialize()
        val mediaItems = mapToExoMediaItems(playlist)
        if (mediaItems.isEmpty()) return
        exoPlayer?.setMediaItems(mediaItems)
        exoPlayer?.prepare()
    }
    
    override fun switchPlaylistAfterCurrent(playlist: Playlist) {
        val player = exoPlayer ?: return
        val newItems = mapToExoMediaItems(playlist)
        if (newItems.isEmpty()) return
        
        val currentIndex = player.currentMediaItemIndex
        if (currentIndex != C.INDEX_UNSET && player.mediaItemCount > 0) {
            // Remove everything after current item
            if (currentIndex + 1 < player.mediaItemCount) {
                player.removeMediaItems(currentIndex + 1, player.mediaItemCount)
            }
            // Add new items
            player.addMediaItems(newItems)
            // Once the current item finishes, it will seamlessly cross into the newItems.
            // Note: Since repeatMode is ALL, it will eventually loop back to the OLD current item
            // unless we remove it. A more robust way is handling the transition event in Executor.
            // For true seamlessness, we queue it here, and remove index 0 when transition happens.
            // But doing it via PlaylistExecutor observing MediaItemTransitioned is cleaner.
        } else {
            setPlaylist(playlist)
        }
    }
    
    override fun isPlaying(): Boolean {
        return exoPlayer?.isPlaying == true
    }

    private fun mapToExoMediaItems(playlist: Playlist): List<MediaItem> {
        return playlist.items.mapNotNull { domainItem ->
            if (!domainItem.isDownloaded) return@mapNotNull null
            val localUri = Uri.parse("file:///data/user/0/com.digitalsignage.player/files/media/${domainItem.mediaId}")

            val builder = MediaItem.Builder()
                .setMediaId(domainItem.mediaId)
                .setUri(localUri)
                
            if (domainItem.mediaType == MediaType.IMAGE) {
                // Media3 1.2.1 supports native timed image playback
                // Fallback: If device has broken image decoder, onPlayerError will trigger and recoverPlayback() will skip it.
                builder.setImageDurationMs(domainItem.durationMs)
            }
            builder.build()
        }
    }

    override fun getCurrentMediaId(): String? = exoPlayer?.currentMediaItem?.mediaId
    override fun getCurrentPosition(): Long = exoPlayer?.currentPosition ?: 0L
    override fun getCurrentPlaylistId(): String? = currentPlaylistId

    override fun play() { exoPlayer?.play() }
    override fun pause() { exoPlayer?.pause() }
    override fun stop() { exoPlayer?.stop() }
    override fun release() {
        watchdogJob?.cancel()
        scope.cancel()
        exoPlayer?.release()
        exoPlayer = null
    }
}


