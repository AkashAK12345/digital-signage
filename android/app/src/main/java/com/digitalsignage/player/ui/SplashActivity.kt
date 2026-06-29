package com.digitalsignage.player.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.digitalsignage.player.BuildConfig
import com.digitalsignage.player.DigitalSignageApplication
import com.digitalsignage.player.databinding.ActivitySplashBinding
import com.digitalsignage.player.network.RetrofitClient
import com.digitalsignage.player.repository.DeviceRepository
import com.digitalsignage.player.repository.Result
import com.digitalsignage.player.storage.DeviceDataStore
import kotlinx.coroutines.launch

@SuppressLint("HardwareIds")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var repository: DeviceRepository
    private lateinit var dataStore: DeviceDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStore = DeviceDataStore(applicationContext)
        repository = DeviceRepository(RetrofitClient.apiService, dataStore)

        DigitalSignageApplication.logger.i("Splash", "App started")

        binding.btnRetry.setOnClickListener {
            startStartupFlow()
        }

        startStartupFlow()
    }

    private fun startStartupFlow() {
        showLoading("Checking credentials...")
        
        lifecycleScope.launch {
            val deviceId = dataStore.getDeviceId()
            val token = dataStore.getDeviceToken()

            if (!deviceId.isNullOrEmpty() && !token.isNullOrEmpty()) {
                DigitalSignageApplication.logger.i("Splash", "Device already registered: $deviceId")
                RetrofitClient.deviceToken = token
                navigateToPlayback(deviceId)
            } else {
                DigitalSignageApplication.logger.i("Splash", "Device not registered. Starting registration.")
                registerDevice()
            }
        }
    }

    private suspend fun registerDevice() {
        showLoading("Registering device...")
        
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID) ?: "unknown_id"
        val name = "Android TV ($androidId)"
        val resolution = "1920x1080" // Default for now
        val appVersion = BuildConfig.VERSION_NAME

        val result = repository.registerDevice(androidId, name, resolution, appVersion)
        
        when (result) {
            is Result.Success -> {
                DigitalSignageApplication.logger.i("Splash", "Registration successful. Device ID: ${result.data}")
                RetrofitClient.deviceToken = dataStore.getDeviceToken()
                navigateToPlayback(result.data)
            }
            is Result.Error -> {
                DigitalSignageApplication.logger.e("Splash", "Registration failed: ${result.message}", result.exception)
                showError("Registration Failed.\n${result.message}")
            }
        }
    }

    private fun showLoading(message: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnRetry.visibility = View.GONE
        binding.tvStatus.text = message
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.btnRetry.visibility = View.VISIBLE
        binding.tvStatus.text = message
        binding.btnRetry.requestFocus()
    }

    private fun navigateToPlayback(deviceId: String) {
        val reason = intent.getStringExtra("startup_reason") ?: "NORMAL"
        val intent = Intent(this, PlaybackActivity::class.java).apply {
            putExtra(PlaybackActivity.EXTRA_DEVICE_ID, deviceId)
            putExtra("startup_reason", reason)
        }
        startActivity(intent)
        finish()
    }
}
