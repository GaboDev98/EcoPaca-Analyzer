package com.gabodev.ecopacaanalyzer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabodev.ecopacaanalyzer.auth.doLoginFirebase
import com.gabodev.ecopacaanalyzer.auth.doLogoutFirebase
import com.gabodev.ecopacaanalyzer.auth.doResetPasswordFirebase
import com.gabodev.ecopacaanalyzer.auth.doSignUpFirebase
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
    private val _authState = MutableStateFlow(AuthState())
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
