package com.digitalsignage.player.player

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.digitalsignage.player.DigitalSignageApplication
import com.digitalsignage.player.network.RetrofitClient
import kotlinx.coroutines.delay

/**
 * Worker responsible for executing the heartbeat POST request.
 * Managed by HeartbeatScheduler using WorkManager.
 */
class HeartbeatWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val TAG = "HeartbeatWorker"
        const val KEY_DEVICE_ID = "device_id"
    }

    private val logger = DigitalSignageApplication.logger

    override suspend fun doWork(): Result {
        val deviceId = inputData.getString(KEY_DEVICE_ID) ?: return Result.failure()

        // Access the central repository
        val repository = DigitalSignageApplication.deviceStateRepository

        logger.i(TAG, "Heartbeat worker started (WorkManager)")

        return try {
            val runtimeSnapshot = repository.getRuntimeSnapshot()
            val healthSnapshot = repository.getHealthSnapshot()
            val capabilities = repository.getDeviceCapabilities()

            if (runtimeSnapshot == null || healthSnapshot == null || capabilities == null) {
                logger.w(TAG, "Snapshots not fully populated yet, skipping heartbeat")
                return Result.retry()
            }

            val sequenceNumber = repository.getNextSequenceNumber()

            val factory = HeartbeatPayloadFactory(deviceId)
            val payload = factory.createPayload(
                runtime = runtimeSnapshot,
                health = healthSnapshot,
                capabilities = capabilities,
                sequenceNumber = sequenceNumber
            )

            logger.i(TAG, "Heartbeat payload created (seq: $sequenceNumber)")
            logger.d(TAG, "Heartbeat queued")

            val apiService = RetrofitClient.apiService
            logger.d(TAG, "Heartbeat sent")
            val response = apiService.sendHeartbeat(payload)

            if (response.isSuccessful) {
                logger.i(TAG, "Heartbeat acknowledged by backend")
                // Success is logged, DeviceStateRepository is the SSOT
                DeviceEventBus.publish(DeviceEvent.HeartbeatSucceeded(sequenceNumber))
                Result.success()
            } else {
                logger.w(TAG, "Heartbeat rejected by backend: HTTP ${response.code()}")
                logger.i(TAG, "Retry scheduled")
                DeviceEventBus.publish(DeviceEvent.HeartbeatFailed(sequenceNumber, response.code()))
                Result.retry()
            }
        } catch (e: Exception) {
            logger.e(TAG, "Heartbeat failed with exception: ${e.message}")
            logger.i(TAG, "Retry failed, scheduling backoff")
            DeviceEventBus.publish(DeviceEvent.HeartbeatFailed(-1, -1))
            Result.retry()
        }
    }
}
