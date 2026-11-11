package com.gabodev.iotdevicemonitor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gabodev.iotdevicemonitor.ui.components.AlertBannerSlot
import com.gabodev.iotdevicemonitor.ui.components.AuthLayout
import com.gabodev.iotdevicemonitor.ui.components.BannerTone
import com.gabodev.iotdevicemonitor.ui.components.PasswordField
import com.gabodev.iotdevicemonitor.ui.theme.Spacing
import com.gabodev.iotdevicemonitor.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onSignUpSuccess: () -> Unit,
    onBackClick: () -> Unit,
) {
    val authState by viewModel.authState.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    var touched by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.clearMessages() }
    LaunchedEffect(authState.isAuthenticated) { if (authState.isAuthenticated) onSignUpSuccess() }

    val shortPassword = touched && password.isNotEmpty() && password.length < 6
    val mismatch = touched && confirm.isNotEmpty() && confirm != password
    val canSubmit = isValidEmail(email) && password.length >= 6 && confirm == password

    AuthLayout(title = "Crear cuenta", subtitle = "Regístrate para agregar y monitorear tus dispositivos.", onBack = onBackClick) {
        AlertBannerSlot(authState.error, BannerTone.Error)
        EmailField(email, { email = it }, enabled = !authState.isLoading, showError = touched)
        PasswordField(
            password, { password = it }, label = "Contraseña", enabled = !authState.isLoading,
            isError = shortPassword, supportingText = "Mínimo 6 caracteres", imeAction = ImeAction.Next,
        )
        PasswordField(
            confirm, { confirm = it }, label = "Confirmar contraseña", enabled = !authState.isLoading,
            isError = mismatch, supportingText = if (mismatch) "Las contraseñas no coinciden" else null,
        )
        PrimaryButton("Crear cuenta", "Creando cuenta…", authState.isLoading, enabled = email.isNotBlank() && password.isNotBlank() && confirm.isNotBlank()) {
            touched = true
            if (canSubmit) viewModel.signUp(email.trim(), password, confirm)
        }
        TextButton(onClick = onBackClick, enabled = !authState.isLoading, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Ya tengo cuenta")
        }
    }
}
