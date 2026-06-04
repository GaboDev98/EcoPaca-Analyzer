package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gabodev.ecopacaanalyzer.viewmodel.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onBackClick: () -> Unit
) {
    val authState = viewModel.authState.collectAsState().value
    var email by remember { mutableStateOf("") }
    var emailSent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "←",
                fontSize = 24.sp,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .clickable(enabled = !authState.isLoading) { onBackClick() }
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Recuperar Contraseña", style = MaterialTheme.typography.h6)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon/Title
            Text(
                "🔐",
                fontSize = 48.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                "Recupera tu acceso",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                "Ingresa tu email y te enviaremos un enlace para restablecer tu contraseña",
                style = MaterialTheme.typography.body2,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Error message
            AnimatedVisibility(visible = authState.error != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    backgroundColor = Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(8.dp),
                    elevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⚠️", fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
                        Text(
                            authState.error ?: "",
                            style = MaterialTheme.typography.body2,
                            color = Color(0xB71C1C)
                        )
                    }
                }
            }

            // Success message
            AnimatedVisibility(visible = authState.successMessage != null && emailSent) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    backgroundColor = Color(0xC8E6C9),
                    shape = RoundedCornerShape(8.dp),
                    elevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("✓", fontSize = 18.sp, color = Color(0x2E7D32), modifier = Modifier.padding(end = 8.dp))
                        Text(
                            authState.successMessage ?: "",
                            style = MaterialTheme.typography.body2,
                            color = Color(0x1B5E20)
                        )
                    }
                }
            }

            if (!emailSent) {
                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    placeholder = { Text("tu@email.com") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    enabled = !authState.isLoading
                )

                // Send button
                Button(
                    onClick = {
                        viewModel.resetPassword(email)
                        emailSent = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = !authState.isLoading && email.isNotEmpty(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (authState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Enviar Email", fontSize = 16.sp)
                    }
                }
            } else {
                // After email sent
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    backgroundColor = Color(0xF5F5F5),
                    shape = RoundedCornerShape(8.dp),
                    elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "✓ Email Enviado",
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            "Revisa tu bandeja de entrada para el enlace de recuperación",
                            style = MaterialTheme.typography.caption,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            "No olvides revisar la carpeta de spam",
                            style = MaterialTheme.typography.caption,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Back to login button
                OutlinedButton(
                    onClick = { onBackClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Volver al Login", fontSize = 16.sp)
                }
            }
        }
    }
}
