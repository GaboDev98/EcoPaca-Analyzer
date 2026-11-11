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
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onBackClick: () -> Unit,
) {
    val authState by viewModel.authState.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    var touched by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.clearMessages() }

    AuthLayout(
        title = "Recuperar contraseña",
        subtitle = "Te enviaremos un enlace para crear una nueva contraseña.",
        onBack = onBackClick,
    ) {
        AlertBannerSlot(authState.error, BannerTone.Error)
        AlertBannerSlot(authState.successMessage, BannerTone.Success)
        EmailField(email, { email = it }, enabled = !authState.isLoading, showError = touched, imeAction = ImeAction.Done)
        PrimaryButton("Enviar enlace", "Enviando…", authState.isLoading, enabled = email.isNotBlank()) {
            touched = true
            if (isValidEmail(email)) viewModel.resetPassword(email.trim())
        }
        TextButton(onClick = onBackClick, enabled = !authState.isLoading, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Volver a iniciar sesión")
        }
    }
}
