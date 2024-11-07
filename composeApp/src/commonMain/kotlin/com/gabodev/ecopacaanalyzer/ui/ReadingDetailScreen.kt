package com.gabodev.ecopacaanalyzer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReadingDetailScreen(viewModel: PacaViewModel, userId: String, readingId: String) {
    val reading = viewModel.readingDetail.collectAsState().value

    LaunchedEffect(userId) {
        viewModel.getReadingDetail(userId, readingId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Reading Detail", style = MaterialTheme.typography.h6)

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