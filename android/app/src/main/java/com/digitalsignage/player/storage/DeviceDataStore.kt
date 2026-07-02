package com.digitalsignage.player.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "device_prefs")

@Singleton
class DeviceDataStore @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private val DEVICE_ID_KEY = stringPreferencesKey("device_id")
        private val DEVICE_TOKEN_KEY = stringPreferencesKey("device_token")
        private val HEARTBEAT_INTERVAL_KEY = intPreferencesKey("heartbeat_interval")
        private val SYNC_INTERVAL_KEY = intPreferencesKey("sync_interval")
        private val DEPLOYMENT_MODE_KEY = stringPreferencesKey("deployment_mode")
        private val MAINTENANCE_PIN_HASH_KEY = stringPreferencesKey("maintenance_pin_hash")
    }

    suspend fun saveDeviceCredentials(deviceId: String, token: String, heartbeat: Int, sync: Int) {
        context.dataStore.edit { prefs ->
            prefs[DEVICE_ID_KEY] = deviceId
            prefs[DEVICE_TOKEN_KEY] = token
            prefs[HEARTBEAT_INTERVAL_KEY] = heartbeat
            prefs[SYNC_INTERVAL_KEY] = sync
        }
    }

    val deviceIdFlow: Flow<String?> = context.dataStore.data.map { it[DEVICE_ID_KEY] }
    val deviceTokenFlow: Flow<String?> = context.dataStore.data.map { it[DEVICE_TOKEN_KEY] }
    val deploymentModeFlow: Flow<String?> = context.dataStore.data.map { it[DEPLOYMENT_MODE_KEY] }
    val maintenancePinHashFlow: Flow<String?> = context.dataStore.data.map { it[MAINTENANCE_PIN_HASH_KEY] }
    
    suspend fun getDeviceId(): String? = deviceIdFlow.first()
    suspend fun getDeviceToken(): String? = deviceTokenFlow.first()
    suspend fun getSyncInterval(): Int = context.dataStore.data.map { it[SYNC_INTERVAL_KEY] ?: 60 }.first()
    suspend fun getHeartbeatInterval(): Int = context.dataStore.data.map { it[HEARTBEAT_INTERVAL_KEY] ?: 60 }.first()
}

