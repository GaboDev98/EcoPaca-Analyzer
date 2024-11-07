package com.gabodev.ecopacaanalyzer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ReadingsScreen(viewModel: PacaViewModel, userId: String, navController: NavHostController) {
    val readings = viewModel.readings.collectAsState().value

    LaunchedEffect(userId) {
        viewModel.loadReadings(userId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Lecturas de la Paca", modifier = Modifier.padding(bottom = 16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(readings.values.toList()) { reading ->
                ReadingItem(reading) {
                    navController.navigate("readingDetail/${userId}/${reading.id}")
                }
            }
        }
    }
}