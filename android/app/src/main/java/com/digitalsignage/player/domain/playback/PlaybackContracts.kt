package com.digitalsignage.player.domain.playback

import com.digitalsignage.player.domain.model.Playlist

// Orchestration Layer (Domain)
interface PlaylistExecutor {
    fun execute(playlist: Playlist)
    fun stop()
}

// Hardware-agnostic Engine Contract (Player layer contract exposed to Domain)
// Moving the interface back to domain/playback to respect Dependency Inversion.
// The concrete ExoPlayer implementation remains in the player package.
interface PlaybackEngine {
    fun initialize()
    fun setPlaylist(playlist: Playlist)
    fun switchPlaylistAfterCurrent(playlist: Playlist)
    fun isPlaying(): Boolean
    fun getCurrentMediaId(): String?
    fun getCurrentPosition(): Long
    fun getCurrentPlaylistId(): String?
    fun play()
    fun pause()
    fun stop()
    fun release()
}


