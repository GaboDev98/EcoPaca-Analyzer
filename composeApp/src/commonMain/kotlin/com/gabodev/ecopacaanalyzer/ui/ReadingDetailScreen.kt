package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gabodev.ecopacaanalyzer.viewmodel.PacaViewModel

@Composable
fun ReadingDetailScreen(
    viewModel: PacaViewModel,
    userId: String,
    readingId: String
) {
    val reading = viewModel.readingDetail.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    LaunchedEffect(userId, readingId) {
        viewModel.getReadingDetail(userId, readingId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Detalle de la lectura", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            reading?.let {
                Text(text = "Timestamp: ${it.timestamp}")
                Text(text = "Humidity: ${it.humidity}")
                Text(text = "Pressure: ${it.pressure}")
                Text(text = "Temperature: ${it.temperature}")
            } ?: run {
                Text(text = "No data available.")
            }
        }
    }
}