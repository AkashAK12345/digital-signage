package com.digitalsignage.player.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.digitalsignage.player.DigitalSignageApplication
import com.digitalsignage.player.ui.SplashActivity

class BootReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "BootReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            DigitalSignageApplication.logger.i(TAG, "Boot received. Restoring application...")
            
            DeviceEventBus.publish(DeviceEvent.BootCompleted)

            val splashIntent = Intent(context, SplashActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                putExtra("startup_reason", StartupReason.BOOT.name)
            }
            context.startActivity(splashIntent)
        }
    }
}
