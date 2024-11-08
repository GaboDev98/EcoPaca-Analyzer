package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            .background(Color.White)
    ) {
        Toolbar(title = "Lecturas de la paca", showBackButton = true, onBackClick = {
            navController.popBackStack()
        })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                if (readings.isNotEmpty()) {
                    val sortedReadings = readings.values
                        .sortedByDescending { it.timestamp.toLongOrNull() ?: Long.MIN_VALUE }

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(sortedReadings, key = { it -> (it.timestamp.takeIf { it.isNotEmpty() } ?: it.id)!! }) { reading ->
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
}