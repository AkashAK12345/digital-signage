package com.digitalsignage.player.player

import com.digitalsignage.player.DigitalSignageApplication

interface PlaybackEngineListener {
    fun onPlaybackStarted(session: PlaybackSession)
    fun onMediaDisplayed(item: PlayableMedia, index: Int, total: Int)
    fun onMediaCompleted(item: PlayableMedia)
    fun onPlaybackError(item: PlayableMedia, message: String)
    fun onSessionReloaded(oldVersion: Long, newVersion: Long)
}

/**
 * Media-type agnostic engine.
 * Delegates actual playback to registered MediaRenderers.
 */
class PlaybackEngine(
    private val renderers: Map<Class<out PlayableMedia>, MediaRenderer>,
    private val listener: PlaybackEngineListener
) {

    companion object {
        private const val TAG = "PlaybackEngine"
    }

    private val logger = DigitalSignageApplication.logger

    private var currentSession: PlaybackSession? = null
    private var isPlaying: Boolean = false
    private var pendingSession: PlaybackSession? = null
    private var activeRenderer: MediaRenderer? = null

    fun startSession(session: PlaybackSession) {
        if (!session.hasItems) {
            logger.w(TAG, "Cannot start session: no playable items")
            return
        }

        val oldVersion = currentSession?.playlistVersion ?: -1L
        val isReload = isPlaying && oldVersion != session.playlistVersion

        if (isReload) {
            logger.i(TAG, "New playlist received (v${session.playlistVersion}), will switch after current item")
            pendingSession = session
            return
        }

        logger.i(TAG, "Starting playback: '${session.playlistName}' (${session.itemCount} items, v${session.playlistVersion})")
        currentSession = session
        isPlaying = true
        listener.onPlaybackStarted(session)
        displayCurrentItem()
    }

    fun stop() {
        logger.i(TAG, "Playback stopped")
        isPlaying = false
        activeRenderer?.stop()
        activeRenderer = null
        currentSession = null
        pendingSession = null
    }

    fun release() {
        stop()
        renderers.values.forEach { it.release() }
    }

    private fun displayCurrentItem() {
        val session = currentSession ?: return
        val item = session.currentItem

        if (item == null) {
            logger.w(TAG, "No item at index ${session.currentIndex}, restarting from 0")
            currentSession = session.withIndex(0)
            displayCurrentItem()
            return
        }

        val renderer = renderers[item::class.java]
        if (renderer == null) {
            val errorMsg = "No renderer registered for media type: ${item::class.java.simpleName}"
            logger.e(TAG, errorMsg)
            listener.onPlaybackError(item, errorMsg)
            advanceToNext()
            return
        }

        activeRenderer?.stop()
        activeRenderer = renderer

        logger.i(TAG, "Renderer selected for ${item.name}: ${renderer::class.java.simpleName}")
        listener.onMediaDisplayed(item, session.currentIndex, session.itemCount)

        renderer.play(
            media = item,
            onComplete = {
                onItemCompleted(item)
            },
            onError = { error ->
                listener.onPlaybackError(item, error)
                advanceToNext()
            }
        )
    }

    private fun onItemCompleted(completedItem: PlayableMedia) {
        logger.d(TAG, "Media completed: ${completedItem.name}")
        listener.onMediaCompleted(completedItem)

        // Check for pending playlist reload
        val session = currentSession ?: return
        val pending = pendingSession
        if (pending != null) {
            val oldVersion = session.playlistVersion
            pendingSession = null
            logger.i(TAG, "Switching playlist: v$oldVersion -> v${pending.playlistVersion}")
            listener.onSessionReloaded(oldVersion, pending.playlistVersion)
            currentSession = pending
            displayCurrentItem()
            return
        }

        advanceToNext()
    }

    private fun advanceToNext() {
        val session = currentSession ?: return
        val nextIdx = session.nextIndex()
        logger.d(TAG, "Advancing to next: index $nextIdx")
        currentSession = session.withIndex(nextIdx)
        displayCurrentItem()
    }
}
