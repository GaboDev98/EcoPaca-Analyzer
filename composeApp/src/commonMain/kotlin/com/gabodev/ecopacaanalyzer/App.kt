package com.gabodev.ecopacaanalyzer

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gabodev.ecopacaanalyzer.ui.ReadingsScreen
import com.gabodev.ecopacaanalyzer.ui.DeviceListScreen
import com.gabodev.ecopacaanalyzer.utils.orEmpty
import com.gabodev.ecopacaanalyzer.viewmodel.PacaViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(viewModel: PacaViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "deviceList") {
        composable("deviceList") {
            DeviceListScreen(viewModel = viewModel, onDeviceClick = { deviceId ->
                navController.navigate("readings/$deviceId")
            })
        }
        composable("readings/{deviceId}") { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getString("deviceId").orEmpty()
            ReadingsScreen(viewModel = viewModel, deviceId = deviceId, navController = navController)
        }
    }
}