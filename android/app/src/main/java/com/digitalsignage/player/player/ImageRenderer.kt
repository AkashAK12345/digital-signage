package com.digitalsignage.player.player

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import coil.load
import coil.request.Disposable
import com.digitalsignage.player.DigitalSignageApplication

class ImageRenderer(
    private val imageView: ImageView
) : MediaRenderer {

    companion object {
        private const val TAG = "ImageRenderer"
        private const val DEFAULT_IMAGE_DURATION_SECONDS = 10
    }

    private val logger = DigitalSignageApplication.logger
    private val handler = Handler(Looper.getMainLooper())
    private var currentDisposable: Disposable? = null
    private var onCompleteCallback: (() -> Unit)? = null

    private val durationRunnable = Runnable {
        onCompleteCallback?.invoke()
    }

    override fun play(media: PlayableMedia, onComplete: () -> Unit, onError: (String) -> Unit) {
        if (media !is PlayableMedia.Image) {
            onError("Unsupported media type for ImageRenderer: ${media::class.java.simpleName}")
            return
        }

        stop()
        this.onCompleteCallback = onComplete

        if (!media.localFile.exists()) {
            logger.e(TAG, "Image file not found: ${media.localFile.absolutePath}")
            onError("File not found: ${media.localFile.name}")
            return
        }

        imageView.visibility = View.VISIBLE
        logger.d(TAG, "Loading image via Coil: ${media.name}")

        currentDisposable = imageView.load(media.localFile) {
            crossfade(true)
            crossfade(400)
            listener(
                onSuccess = { _, _ ->
                    logger.i(TAG, "Image displayed: ${media.name}")
                    val durationMs = (media.durationSeconds.takeIf { it > 0 } ?: DEFAULT_IMAGE_DURATION_SECONDS) * 1000L
                    handler.postDelayed(durationRunnable, durationMs)
                },
                onError = { _, result ->
                    val error = result.throwable.message ?: "Unknown Coil error"
                    logger.e(TAG, "Image load failed: ${media.name}: $error")
                    onError(error)
                }
            )
        }
    }

    override fun stop() {
        handler.removeCallbacks(durationRunnable)
        currentDisposable?.dispose()
        currentDisposable = null
        onCompleteCallback = null
        imageView.visibility = View.GONE
    }

    override fun release() {
        stop()
    }
}
