package com.gabodev.ecopacaanalyzer

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(viewModel: PacaViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "userList") {
        composable("userList") {
            UserListScreen(viewModel = viewModel, onUserClick = { userId ->
                navController.navigate("readings/$userId")
            })
        }
        composable("readings/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ReadingsScreen(viewModel = viewModel, userId = userId)
        }
    }
}