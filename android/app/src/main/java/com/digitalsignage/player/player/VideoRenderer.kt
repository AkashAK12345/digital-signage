package com.digitalsignage.player.player

import android.view.View
import androidx.media3.ui.PlayerView
import com.digitalsignage.player.DigitalSignageApplication

class VideoRenderer(
    private val playerView: PlayerView,
    private val adapter: VideoPlayerAdapter
) : MediaRenderer {

    companion object {
        private const val TAG = "VideoRenderer"
    }

    private val logger = DigitalSignageApplication.logger

    override fun play(media: PlayableMedia, onComplete: () -> Unit, onError: (String) -> Unit) {
        if (media !is PlayableMedia.Video) {
            onError("Unsupported media type for VideoRenderer: ${media::class.java.simpleName}")
            return
        }

        if (!media.localFile.exists()) {
            val error = "Video file not found: ${media.localFile.name}"
            logger.e(TAG, error)
            onError(error)
            return
        }

        playerView.visibility = View.VISIBLE
        logger.i(TAG, "Delegating video playback to VideoPlayerAdapter: ${media.name}")
        
        adapter.play(
            file = media.localFile,
            onComplete = {
                logger.i(TAG, "Video completed naturally: ${media.name}")
                onComplete()
            },
            onError = { error ->
                logger.e(TAG, "Video playback failed: ${media.name}, error: $error")
                onError(error)
            }
        )
    }

    override fun stop() {
        adapter.stop()
        playerView.visibility = View.GONE
    }

    override fun release() {
        adapter.release()
    }
}
