package com.digitalsignage.player.data.repository

import com.digitalsignage.player.core.error.AppError
import com.digitalsignage.player.core.identity.DeviceIdentityManager
import com.digitalsignage.player.core.logging.Logger
import com.digitalsignage.player.data.local.datastore.RuntimeConfigStoreImpl
import com.digitalsignage.player.domain.registration.RegistrationState
import com.digitalsignage.player.domain.repository.DeviceRepository
import com.digitalsignage.player.domain.repository.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepositoryImpl @Inject constructor(
    private val runtimeConfigStore: RuntimeConfigStoreImpl,
    private val identityManager: DeviceIdentityManager,
    private val logger: Logger
) : DeviceRepository { 

    private val _registrationState = MutableStateFlow(RegistrationState.Unregistered)

    override suspend fun registerDevice(): Result<Boolean> {
        _registrationState.value = RegistrationState.Registering
        return try {
            val installId = runtimeConfigStore.getOrCreateInstallationId { identityManager.generateAppInstallationId() }
            val metadata = identityManager.getDeviceMetadata()
            
            logger.i("DeviceRepository", "Attempting idempotent registration for UUID: ${installId}")
            logger.d("DeviceRepository", "Diagnostic Metadata: ${metadata}")
            
            // Stubbed API call. The backend treats this operation as idempotent.
            // If the installId already exists, it returns the existing token or refreshes it.
            val success = true 
            val isBanned = false
            
            if (isBanned) {
                _registrationState.value = RegistrationState.Banned
                return Result.Error(AppError.Fatal("Device is permanently banned."))
            }
            
            if (success) {
                runtimeConfigStore.saveDeviceToken("token_${installId}")
                _registrationState.value = RegistrationState.Registered
                Result.Success(true)
            } else {
                _registrationState.value = RegistrationState.RegistrationFailed
                Result.Error(AppError.Recoverable("API returned unauthorized or failed"))
            }
        } catch (e: Exception) {
            _registrationState.value = RegistrationState.RegistrationFailed
            Result.Error(AppError.Retryable("Network or Server error during registration", e))
        }
    }
    
    override fun observeRegistrationState(): Flow<Boolean> = runtimeConfigStore.isRegistered
    
    suspend fun validateLocalCredentials(): Boolean {
        val token = runtimeConfigStore.deviceToken.firstOrNull()
        return if (!token.isNullOrBlank()) {
            _registrationState.value = RegistrationState.Registered
            true
        } else {
            _registrationState.value = RegistrationState.Unregistered
            false
        }
    }
}

