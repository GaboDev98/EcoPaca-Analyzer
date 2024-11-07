package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gabodev.ecopacaanalyzer.models.Reading
import com.gabodev.ecopacaanalyzer.utils.toFormattedDate

@Composable
fun ReadingItem(reading: Reading, onClick: () -> Unit) {
    val formattedDate = reading.timestamp.toLong().toFormattedDate()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Fecha: $formattedDate", style = MaterialTheme.typography.body1)
            Text("Humedad: ${reading.humidity}", style = MaterialTheme.typography.body2)
            Text("Presión: ${reading.pressure}", style = MaterialTheme.typography.body2)
            Text("Temperatura: ${reading.temperature}", style = MaterialTheme.typography.body2)
        }
    }
}