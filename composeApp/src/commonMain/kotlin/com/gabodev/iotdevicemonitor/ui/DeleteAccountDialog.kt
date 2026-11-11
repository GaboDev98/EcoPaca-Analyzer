package com.gabodev.iotdevicemonitor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gabodev.iotdevicemonitor.ui.components.AlertBanner
import com.gabodev.iotdevicemonitor.ui.components.BannerTone
import com.gabodev.iotdevicemonitor.ui.components.PasswordField
import com.gabodev.iotdevicemonitor.ui.theme.Spacing
import com.gabodev.iotdevicemonitor.viewmodel.AuthViewModel

/**
 * Permanent account deletion (Google Play requirement). Asks for the password because
 * Firebase only deletes accounts with a recent login.
 */
@Composable
fun DeleteAccountDialog(
    authViewModel: AuthViewModel,
    onDismiss: () -> Unit,
    onDeleted: () -> Unit,
) {
    val state by authViewModel.authState.collectAsState()
    var password by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { authViewModel.clearMessages() }
    LaunchedEffect(state.isAuthenticated) {
        if (!state.isAuthenticated) onDeleted()
    }

    AlertDialog(
        onDismissRequest = { if (!state.isLoading) onDismiss() },
        icon = { Icon(Icons.Rounded.DeleteForever, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
        title = { Text("¿Eliminar tu cuenta?") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
                Text(
                    "Se borrarán tu cuenta, todos tus dispositivos y su historial de lecturas. " +
                        "Esta acción no se puede deshacer.",
                    style = MaterialTheme.typography.bodyMedium,
                )
                PasswordField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Contraseña",
                    enabled = !state.isLoading,
                    isError = state.error != null,
                )
                state.error?.let { AlertBanner(it, BannerTone.Error) }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { authViewModel.deleteAccount(password) },
                enabled = !state.isLoading && password.isNotEmpty(),
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                } else {
                    Text("Eliminar cuenta")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !state.isLoading) { Text("Cancelar") }
        },
    )
}
