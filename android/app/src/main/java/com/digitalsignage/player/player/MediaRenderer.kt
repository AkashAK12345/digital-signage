package com.digitalsignage.player.player

/**
 * Interface for all media renderers.
 * PlaybackEngine interacts only with this abstraction.
 */
interface MediaRenderer {
    /**
     * Start playing the given media item.
     * @param media The media item to play.
     * @param onComplete Callback invoked when the media naturally completes its duration or playback.
     * @param onError Callback invoked if playback fails.
     */
    fun play(media: PlayableMedia, onComplete: () -> Unit, onError: (String) -> Unit)

    /**
     * Stop current playback and hide renderer UI.
     */
    fun stop()

    /**
     * Release all resources.
     */
    fun release()
}
