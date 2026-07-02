package com.digitalsignage.player.domain.playback.executor

import com.digitalsignage.player.core.event.PlayerEvent
import com.digitalsignage.player.core.event.PlayerEventBus
import com.digitalsignage.player.core.logging.Logger
import com.digitalsignage.player.domain.model.Playlist
import com.digitalsignage.player.domain.playback.PlaybackEngine
import com.digitalsignage.player.domain.playback.PlaylistExecutor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistExecutorImpl @Inject constructor(
    private val playbackEngine: PlaybackEngine,
    private val eventBus: PlayerEventBus,
    private val logger: Logger
) : PlaylistExecutor {

    private var currentPlaylistId: String? = null
    private var pendingPlaylist: Playlist? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        scope.launch {
            eventBus.events.collect { event ->
                if (event is PlayerEvent.MediaItemTransitioned || event is PlayerEvent.PlaylistLooped) {
                    if (pendingPlaylist != null) {
                        logger.i("PlaylistExecutor", "Applying pending playlist after current item finished.")
                        val p = pendingPlaylist
                        pendingPlaylist = null
                        currentPlaylistId = p?.playlistId
                        if (p != null) {
                            playbackEngine.setPlaylist(p)
                            playbackEngine.play()
                        }
                    }
                }
            }
        }
    }

    override fun execute(playlist: Playlist) {
        if (playlist.playlistId == currentPlaylistId) {
            return
        }
        
        logger.i("PlaylistExecutor", "Request to execute playlist: ${playlist.playlistId}")
        
        playbackEngine.initialize()
        
        if (playbackEngine.isPlaying()) {
            logger.i("PlaylistExecutor", "Playback is active. Queueing as pending to finish current item.")
            pendingPlaylist = playlist
        } else {
            currentPlaylistId = playlist.playlistId
            playbackEngine.setPlaylist(playlist)
            playbackEngine.play()
        }
    }

    override fun stop() {
        logger.i("PlaylistExecutor", "Stopping playlist execution.")
        playbackEngine.stop()
        currentPlaylistId = null
        pendingPlaylist = null
    }
}
