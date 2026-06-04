package com.gabodev.ecopacaanalyzer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            try {
                loginFirebase(email, password)
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    isAuthenticated = true,
                    userEmail = email
                )
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al iniciar sesión"
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
            try {
                signUpFirebase(email, password)
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    isAuthenticated = true,
                    userEmail = email,
                    successMessage = "Cuenta creada exitosamente"
                )
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al crear cuenta"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                logoutFirebase()
                _authState.value = AuthState()
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    error = "Error al cerrar sesión"
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
            try {
                resetPasswordFirebase(email)
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    successMessage = "Email de recuperación enviado a $email"
                )
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al enviar email"
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

    // Métodos stub - serán implementados en plataformas específicas
    private suspend fun loginFirebase(email: String, password: String) {
        // Implementación en androidMain/appleMain
        throw Exception("No implementado")
    }

    private suspend fun signUpFirebase(email: String, password: String) {
        // Implementación en androidMain/appleMain
        throw Exception("No implementado")
    }

    private suspend fun logoutFirebase() {
        // Implementación en androidMain/appleMain
    }

    private suspend fun resetPasswordFirebase(email: String) {
        // Implementación en androidMain/appleMain
        throw Exception("No implementado")
    }
}
