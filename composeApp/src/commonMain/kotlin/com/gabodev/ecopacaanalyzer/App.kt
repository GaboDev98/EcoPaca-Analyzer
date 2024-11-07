package com.gabodev.ecopacaanalyzer

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gabodev.ecopacaanalyzer.ui.ReadingDetailScreen
import com.gabodev.ecopacaanalyzer.ui.ReadingsScreen
import com.gabodev.ecopacaanalyzer.ui.UserListScreen
import com.gabodev.ecopacaanalyzer.utils.orEmpty
import com.gabodev.ecopacaanalyzer.viewmodel.PacaViewModel
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
            val userId = backStackEntry.arguments?.getString("userId").orEmpty()
            ReadingsScreen(viewModel = viewModel, userId = userId, navController = navController)
        }
        composable("readingDetail/{userId}/{readingId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId").orEmpty()
            val readingId = backStackEntry.arguments?.getString("readingId").orEmpty()
            ReadingDetailScreen(viewModel = viewModel, userId = userId, readingId = readingId)
        }
    }
}