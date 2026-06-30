package com.digitalsignage.player

import android.app.Application
import com.digitalsignage.player.player.PlaybackController
import com.digitalsignage.player.util.AndroidLogger
import com.digitalsignage.player.util.Logger

class DigitalSignageApplication : Application() {

    companion object {
        lateinit var logger: Logger
            private set
            
        val deviceStateRepository = com.digitalsignage.player.repository.DeviceStateRepository()
        lateinit var crashRecoveryManager: com.digitalsignage.player.player.CrashRecoveryManager
            private set
        lateinit var connectivityObserver: com.digitalsignage.player.network.ConnectivityObserver
            private set
        var playbackController: PlaybackController? = null
    }

    override fun onCreate() {
        super.onCreate()
        
        // Initialize simple Logcat logger
        logger = object : Logger {
            override fun d(tag: String, message: String) {
                android.util.Log.d(tag, message)
            }
            override fun i(tag: String, message: String) {
                android.util.Log.i(tag, message)
            }
            override fun w(tag: String, message: String) {
                android.util.Log.w(tag, message)
            }
            override fun e(tag: String, message: String, throwable: Throwable?) {
                if (throwable != null) {
                    android.util.Log.e(tag, message, throwable)
                } else {
                    android.util.Log.e(tag, message)
                }
            }
        }
        
        crashRecoveryManager = com.digitalsignage.player.player.CrashRecoveryManager(this)
        crashRecoveryManager.recordStartup()
        
        connectivityObserver = com.digitalsignage.player.network.ConnectivityObserver(this)
        
        logger.i("DigitalSignageApplication", "Application started successfully")
    }
}
