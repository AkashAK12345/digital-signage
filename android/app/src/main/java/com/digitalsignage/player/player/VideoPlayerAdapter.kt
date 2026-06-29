package com.digitalsignage.player.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.digitalsignage.player.DigitalSignageApplication
import java.io.File

/**
 * Encapsulates Media3 ExoPlayer instance and configuration.
 * PlaybackEngine and VideoRenderer must never directly manipulate ExoPlayer.
 * This class ensures proper memory management for low-RAM TVs (releasing when unused).
 */
class VideoPlayerAdapter(
    private val context: Context,
    private val playerView: PlayerView
) {
    companion object {
        private const val TAG = "VideoPlayerAdapter"
    }

    private val logger = DigitalSignageApplication.logger
    private var exoPlayer: ExoPlayer? = null
    private var onCompletionCallback: (() -> Unit)? = null
    private var onErrorCallback: ((String) -> Unit)? = null

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_READY -> logger.d(TAG, "ExoPlayer STATE_READY")
                Player.STATE_ENDED -> {
                    logger.d(TAG, "ExoPlayer STATE_ENDED")
                    onCompletionCallback?.invoke()
                }
                Player.STATE_BUFFERING -> logger.d(TAG, "ExoPlayer STATE_BUFFERING")
                Player.STATE_IDLE -> logger.d(TAG, "ExoPlayer STATE_IDLE")
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            val errorMsg = "ExoPlayer error: ${error.message}"
            logger.e(TAG, errorMsg)
            onErrorCallback?.invoke(errorMsg)
        }
    }

    private fun ensurePlayer() {
        if (exoPlayer == null) {
            logger.d(TAG, "Initializing ExoPlayer")
            exoPlayer = ExoPlayer.Builder(context).build().apply {
                addListener(playerListener)
                playWhenReady = true
            }
            playerView.player = exoPlayer
        }
    }

    fun play(file: File, onComplete: () -> Unit, onError: (String) -> Unit) {
        this.onCompletionCallback = onComplete
        this.onErrorCallback = onError
        
        ensurePlayer()
        
        val uri = android.net.Uri.fromFile(file)
        val mediaItem = MediaItem.fromUri(uri)
        
        logger.i(TAG, "Preparing video: ${file.name}")
        exoPlayer?.setMediaItem(mediaItem)
        exoPlayer?.prepare()
    }

    fun stop() {
        logger.d(TAG, "Stopping video playback")
        onCompletionCallback = null
        onErrorCallback = null
        exoPlayer?.stop()
        exoPlayer?.clearMediaItems()
        playerView.player = null
    }

    fun release() {
        logger.d(TAG, "Releasing ExoPlayer")
        stop()
        exoPlayer?.removeListener(playerListener)
        exoPlayer?.release()
        exoPlayer = null
    }
}
