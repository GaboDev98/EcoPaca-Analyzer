package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gabodev.ecopacaanalyzer.models.Reading
import com.gabodev.ecopacaanalyzer.utils.toFormattedDate

@Composable
fun ReadingItem(reading: Reading, isPadding: Boolean = true, showDateLabel: Boolean = true) {
    Column(
        modifier = Modifier.then(
            if (isPadding) Modifier.padding(16.dp) else Modifier
        )
    ) {
        val formattedDate = reading.timestamp.toLongOrNull()?.toFormattedDate() ?: reading.timestamp
        if (showDateLabel) {
            Text("Fecha y hora: $formattedDate", style = MaterialTheme.typography.body2)
        }

        if (reading.sensors.isNotEmpty()) {
            SensorDisplay(reading.sensors)
        } else {
            reading.temperature?.let {
                Text("Temperatura: $it °C", style = MaterialTheme.typography.body2)
            }
            reading.humidity?.let {
                Text("Humedad: $it%", style = MaterialTheme.typography.body2)
            }
            reading.pressure?.let {
                Text("Presión: $it hPa", style = MaterialTheme.typography.body2)
            }
        }
    }
}