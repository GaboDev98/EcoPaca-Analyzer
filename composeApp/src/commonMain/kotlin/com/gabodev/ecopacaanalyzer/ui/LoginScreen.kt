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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gabodev.ecopacaanalyzer.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val authState = viewModel.authState.collectAsState().value
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            "EcoPaca",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            "Monitor de Dispositivos IoT",
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

        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("tu@email.com") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            enabled = !authState.isLoading
        )

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            placeholder = { Text("••••••••") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            enabled = !authState.isLoading
        )

        // Forgot password link
        Text(
            "¿Olvidaste tu contraseña?",
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .align(Alignment.End)
                .clickable(enabled = !authState.isLoading) { onForgotPasswordClick() }
                .padding(bottom = 24.dp)
        )

        // Login button
        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !authState.isLoading && email.isNotEmpty() && password.isNotEmpty(),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (authState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text("Iniciar Sesión", fontSize = 16.sp)
            }
        }

        // Divider
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(modifier = Modifier.weight(1f))
            Text(
                "¿No tienes cuenta?",
                style = MaterialTheme.typography.caption,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Divider(modifier = Modifier.weight(1f))
        }

        // Sign up button
        OutlinedButton(
            onClick = { onSignUpClick() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !authState.isLoading,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Crear Cuenta", fontSize = 16.sp)
        }
    }
}
