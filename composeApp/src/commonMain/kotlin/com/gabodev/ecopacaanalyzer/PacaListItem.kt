package com.gabodev.ecopacaanalyzer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PacaListItem(reading: PacaReading, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onClick() },
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Timestamp: ${reading.timestamp}")
            Text(text = "Temperatura: ${reading.temperature}°C")
            Text(text = "Humedad: ${reading.humidity}%")
            Text(text = "Presión: ${reading.pressure} hPa")
        }
    }
}