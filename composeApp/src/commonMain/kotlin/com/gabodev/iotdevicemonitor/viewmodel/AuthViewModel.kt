package com.gabodev.iotdevicemonitor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabodev.iotdevicemonitor.auth.doDeleteAccountFirebase
import com.gabodev.iotdevicemonitor.auth.doLoginFirebase
import com.gabodev.iotdevicemonitor.auth.doLogoutFirebase
import com.gabodev.iotdevicemonitor.auth.doResetPasswordFirebase
import com.gabodev.iotdevicemonitor.auth.doSignUpFirebase
import com.gabodev.iotdevicemonitor.auth.currentSessionUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val isAuthenticated: Boolean = false,
    val userId: String? = null,
    val userEmail: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

class AuthViewModel : ViewModel() {
    // Firebase persiste la sesión: si ya hay usuario, arrancamos autenticados.
    private val _authState = MutableStateFlow(
        currentSessionUser()?.let { AuthState(isAuthenticated = true, userId = it.uid, userEmail = it.email) }
            ?: AuthState()
    )
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        if (!validateInputs(email, password)) {
            _authState.value = _authState.value.copy(error = "Email o contraseña inválidos")
            return
        }

        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            doLoginFirebase(email, password).onSuccess { userId ->
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    isAuthenticated = true,
                    userId = userId,
                    userEmail = email,
                    error = null
                )
            }.onFailure { error ->
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Error al iniciar sesión"
                )
            }
        }
    }

    fun signUp(email: String, password: String, confirmPassword: String) {
        if (!validateSignUp(email, password, confirmPassword)) {
            _authState.value = _authState.value.copy(error = "Validación fallida")
            return
        }

        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            doSignUpFirebase(email, password).onSuccess { userId ->
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    isAuthenticated = true,
                    userId = userId,
                    userEmail = email,
                    successMessage = "Cuenta creada. Verifica tu email.",
                    error = null
                )
            }.onFailure { error ->
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Error al crear cuenta"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            doLogoutFirebase().onSuccess {
                _authState.value = AuthState()
            }.onFailure { error ->
                _authState.value = _authState.value.copy(
                    error = error.message ?: "Error al cerrar sesión"
                )
            }
        }
    }

    fun deleteAccount(password: String) {
        if (password.isEmpty()) {
            _authState.value = _authState.value.copy(error = "Ingresa tu contraseña para confirmar")
            return
        }
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            doDeleteAccountFirebase(password).onSuccess {
                _authState.value = AuthState()
            }.onFailure { error ->
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = error.message ?: "No pudimos eliminar tu cuenta"
                )
            }
        }
    }

    fun resetPassword(email: String) {
        if (email.isEmpty() || !isValidEmail(email)) {
            _authState.value = _authState.value.copy(error = "Email inválido")
            return
        }

        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            doResetPasswordFirebase(email).onSuccess {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    successMessage = "Email de recuperación enviado a $email",
                    error = null
                )
            }.onFailure { error ->
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Error al enviar email"
                )
            }
        }
    }

    fun clearMessages() {
        _authState.value = _authState.value.copy(error = null, successMessage = null)
    }

    // Métodos privados de validación
    private fun validateInputs(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty() && isValidEmail(email) && password.length >= 6
    }

    private fun validateSignUp(email: String, password: String, confirmPassword: String): Boolean {
        return email.isNotEmpty() &&
            password.isNotEmpty() &&
            confirmPassword.isNotEmpty() &&
            isValidEmail(email) &&
            password.length >= 6 &&
            password == confirmPassword
    }

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }
}
