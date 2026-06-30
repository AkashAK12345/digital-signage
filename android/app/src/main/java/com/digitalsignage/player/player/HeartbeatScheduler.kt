package com.digitalsignage.player.player

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.digitalsignage.player.DigitalSignageApplication
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import androidx.work.OneTimeWorkRequestBuilder

/**
 * Responsibilities:
 * - configure WorkManager
 * - periodic scheduling
 * - manual scheduling
 * - retry scheduling
 */
class HeartbeatScheduler(private val context: Context) {

    companion object {
        private const val TAG = "HeartbeatScheduler"
        private const val HEARTBEAT_WORK_NAME = "periodic_heartbeat"
    }

    private val logger = DigitalSignageApplication.logger
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    init {
        scope.launch {
            DeviceEventBus.events.collect { event ->
                if (event is DeviceEvent.ConnectivityRestored) {
                    DigitalSignageApplication.logger.i("HeartbeatScheduler", "Forcing heartbeat due to connectivity restored")
                    scheduleImmediateHeartbeat()
                }
            }
        }
    }

    fun schedulePeriodicHeartbeat(deviceId: String, intervalSeconds: Long) {
        logger.i(TAG, "Scheduling periodic heartbeat every $intervalSeconds seconds")

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<HeartbeatWorker>(intervalSeconds, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                15, // 15 seconds initial backoff for retries
                TimeUnit.SECONDS
            )
            .setInputData(workDataOf(HeartbeatWorker.KEY_DEVICE_ID to deviceId))
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            HEARTBEAT_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE, // Update if interval changed
            request
        )
    }

    fun cancelHeartbeats() {
        logger.i(TAG, "Cancelling periodic heartbeats")
        WorkManager.getInstance(context).cancelUniqueWork(HEARTBEAT_WORK_NAME)
    }

    private fun scheduleImmediateHeartbeat() {
        logger.i(TAG, "Scheduling immediate heartbeat")
        val request = OneTimeWorkRequestBuilder<HeartbeatWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}
