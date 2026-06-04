package com.gabodev.ecopacaanalyzer

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gabodev.ecopacaanalyzer.ui.*
import com.gabodev.ecopacaanalyzer.utils.orEmpty
import com.gabodev.ecopacaanalyzer.viewmodel.AuthViewModel
import com.gabodev.ecopacaanalyzer.viewmodel.PacaViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(viewModel: PacaViewModel, authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val authState = authViewModel.authState.collectAsState().value

    // Determinar pantalla inicial basada en autenticación
    val startDestination = if (authState.isAuthenticated) "deviceList" else "login"

    NavHost(navController, startDestination = startDestination) {
        // Auth routes
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("deviceList") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSignUpClick = { navController.navigate("signup") },
                onForgotPasswordClick = { navController.navigate("forgotPassword") }
            )
        }

        composable("signup") {
            SignUpScreen(
                viewModel = authViewModel,
                onSignUpSuccess = {
                    navController.navigate("deviceList") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("forgotPassword") {
            ForgotPasswordScreen(
                viewModel = authViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Device routes
        composable("deviceList") {
            DeviceListScreen(
                viewModel = viewModel,
                onDeviceClick = { deviceId ->
                    navController.navigate("readings/$deviceId")
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("deviceList") { inclusive = true }
                    }
                }
            )
        }

        composable("readings/{deviceId}") { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getString("deviceId").orEmpty()
            ReadingsScreen(
                viewModel = viewModel,
                deviceId = deviceId,
                navController = navController
            )
        }
    }
}