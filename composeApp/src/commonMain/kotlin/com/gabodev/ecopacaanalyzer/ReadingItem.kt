package com.gabodev.ecopacaanalyzer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReadingItem(reading: Reading) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text("Timestamp: ${reading.timestamp}")
        Text("Humedad: ${reading.humidity}")
        Text("Presión: ${reading.pressure}")
        Text("Temperatura: ${reading.temperature}")
    }
}