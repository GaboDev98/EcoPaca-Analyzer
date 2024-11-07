package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gabodev.ecopacaanalyzer.viewmodel.PacaViewModel

@Composable
fun ReadingsScreen(viewModel: PacaViewModel, userId: String, navController: NavHostController) {
    val readings = viewModel.readings.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    LaunchedEffect(userId) {
        viewModel.loadReadings(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Lecturas de la paca",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            if (readings.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(readings.values.toList()) { reading ->
                        ReadingItem(reading) {
                            navController.navigate("readingDetail/${userId}/${reading.id}")
                        }
                    }
                }
            } else {
                Text(
                    text = "Lecturas no disponibles.",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}