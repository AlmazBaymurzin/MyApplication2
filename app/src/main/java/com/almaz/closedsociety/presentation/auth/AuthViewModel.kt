package com.almaz.closedsociety.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.almaz.closedsociety.data.repository.UserRepository
import com.almaz.closedsociety.data.security.SignatureManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import com.almaz.closedsociety.data.model.User // Добавить этот импорт

class AuthViewModel(
    private val signatureManager: SignatureManager,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkExistingUser()
    }

    private fun checkExistingUser() {
        val userId = signatureManager.getUserId()
        if (userId != null) {
            _authState.value = AuthState.SignatureRequired
        } else {
            _authState.value = AuthState.RegistrationRequired
        }
    }

    fun onSignupCompleted(publicNickname: String, signatureBitmap: ByteArray) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading

                val uuid = UUID.randomUUID().toString()

                // Сохранение локально
                signatureManager.saveSignatureTemplate(signatureBitmap)
                signatureManager.saveUserId(uuid)

                // Сохранение в Firestore
                val user = User(
                    uuid = uuid,
                    publicNickname = publicNickname,
                    fcmToken = "" // TODO: Добавить FCM токен позже
                )
                userRepository.createUser(user)

                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Ошибка регистрации: ${e.message}")
            }
        }
    }

    fun onSignatureDrawn(signatureBitmap: ByteArray) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading

                val isAuthenticated = verifySignature(signatureBitmap)
                if (isAuthenticated) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error("Подпись не совпадает")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Ошибка аутентификации: ${e.message}")
            }
        }
    }

    private suspend fun verifySignature(currentSignature: ByteArray): Boolean {
        val savedTemplate = signatureManager.getSignatureTemplate()
        // Простое сравнение для примера - в реальности нужна более сложная логика
        return savedTemplate?.contentEquals(currentSignature) ?: false
    }

    fun logout() {
        signatureManager.clearAllData()
        _authState.value = AuthState.RegistrationRequired
    }

    sealed class AuthState {
        data object Initial : AuthState()
        data object RegistrationRequired : AuthState()
        data object SignatureRequired : AuthState()
        data object Authenticated : AuthState()
        data object Loading : AuthState()
        data class Error(val message: String) : AuthState()
    }
}