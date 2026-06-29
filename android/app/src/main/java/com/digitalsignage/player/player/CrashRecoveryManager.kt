package com.digitalsignage.player.player

import android.content.Context
import android.content.SharedPreferences
import com.digitalsignage.player.DigitalSignageApplication
import kotlin.system.exitProcess

class CrashRecoveryManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("crash_recovery", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LAST_SUCCESSFUL_STARTUP = "last_successful_startup"
        private const val KEY_LAST_GRACEFUL_SHUTDOWN = "last_graceful_shutdown"
        private const val KEY_LAST_CRASH_TIMESTAMP = "last_crash_timestamp"
        private const val KEY_CRASH_COUNT_24H = "crash_count_24h"
        
        private const val CRASH_LOOP_THRESHOLD = 3
        private const val CRASH_LOOP_TIME_WINDOW_MS = 5 * 60 * 1000L // 5 minutes
    }

    init {
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            handleUncaughtException(thread, throwable)
        }
    }

    fun recordStartup() {
        val now = System.currentTimeMillis()
        val lastShutdown = prefs.getLong(KEY_LAST_GRACEFUL_SHUTDOWN, 0L)
        val lastStartup = prefs.getLong(KEY_LAST_SUCCESSFUL_STARTUP, 0L)
        
        // If last shutdown is older than last startup, we crashed
        if (lastStartup > lastShutdown && lastStartup > 0L) {
            DigitalSignageApplication.logger.w("CrashRecovery", "Detected unclean shutdown from previous session")
            recordCrash(lastStartup)
        }
        
        prefs.edit().putLong(KEY_LAST_SUCCESSFUL_STARTUP, now).apply()
        DigitalSignageApplication.logger.i("CrashRecovery", "Startup recorded at $now")
    }

    fun recordGracefulShutdown() {
        val now = System.currentTimeMillis()
        prefs.edit().putLong(KEY_LAST_GRACEFUL_SHUTDOWN, now).apply()
        DigitalSignageApplication.logger.i("CrashRecovery", "Graceful shutdown recorded at $now")
    }

    private fun handleUncaughtException(thread: Thread, throwable: Throwable) {
        val now = System.currentTimeMillis()
        DigitalSignageApplication.logger.e("CrashRecovery", "FATAL UNCAUGHT EXCEPTION: ${throwable.message}")
        recordCrash(now)
        exitProcess(1)
    }

    private fun recordCrash(timestamp: Long) {
        val lastCrash = prefs.getLong(KEY_LAST_CRASH_TIMESTAMP, 0L)
        var crashCount = prefs.getInt(KEY_CRASH_COUNT_24H, 0)
        
        // Reset count if outside 24h window
        if (timestamp - lastCrash > 24 * 60 * 60 * 1000L) {
            crashCount = 0
        }
        
        crashCount++
        
        prefs.edit()
            .putLong(KEY_LAST_CRASH_TIMESTAMP, timestamp)
            .putInt(KEY_CRASH_COUNT_24H, crashCount)
            .apply()
            
        if (crashCount >= CRASH_LOOP_THRESHOLD && (timestamp - lastCrash) < CRASH_LOOP_TIME_WINDOW_MS) {
            DigitalSignageApplication.logger.e("CrashRecovery", "CRASH LOOP DETECTED. Blocking aggressive restarts.")
            // Prevent immediate restart loop (placeholder for halting components or entering safe mode)
            Thread.sleep(10000) // Stall 10s before allowing OS to restart us
        }
    }
    
    fun getCrashCount24Hours(): Int = prefs.getInt(KEY_CRASH_COUNT_24H, 0)
    fun getLastCrashTimestamp(): Long = prefs.getLong(KEY_LAST_CRASH_TIMESTAMP, 0L)
    fun getLastSuccessfulStartup(): Long = prefs.getLong(KEY_LAST_SUCCESSFUL_STARTUP, 0L)
    fun getLastGracefulShutdown(): Long = prefs.getLong(KEY_LAST_GRACEFUL_SHUTDOWN, 0L)
}
