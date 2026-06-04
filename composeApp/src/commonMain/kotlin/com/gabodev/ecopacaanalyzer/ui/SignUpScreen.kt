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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gabodev.ecopacaanalyzer.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onSignUpSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val authState = viewModel.authState.collectAsState().value
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var agreeTerms by remember { mutableStateOf(false) }

    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            onSignUpSuccess()
        }
    }

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
            Text("Crear Cuenta", style = MaterialTheme.typography.h6)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                placeholder = { Text("Mín. 6 caracteres") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                enabled = !authState.isLoading
            )

            // Confirm password field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                placeholder = { Text("Repite tu contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                enabled = !authState.isLoading,
                isError = password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword
            )

            // Password mismatch warning
            if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
                Text(
                    "Las contraseñas no coinciden",
                    style = MaterialTheme.typography.caption,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 8.dp)
                )
            }

            // Terms checkbox
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreeTerms,
                    onCheckedChange = { agreeTerms = it },
                    enabled = !authState.isLoading
                )
                Text(
                    "Acepto los términos y condiciones",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Sign up button
            Button(
                onClick = { viewModel.signUp(email, password, confirmPassword) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !authState.isLoading &&
                    email.isNotEmpty() &&
                    password.isNotEmpty() &&
                    confirmPassword.isNotEmpty() &&
                    password == confirmPassword &&
                    agreeTerms,
                shape = RoundedCornerShape(8.dp)
            ) {
                if (authState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Crear Cuenta", fontSize = 16.sp)
                }
            }
        }
    }
}
