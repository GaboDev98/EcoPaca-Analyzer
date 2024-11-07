package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gabodev.ecopacaanalyzer.models.Reading
import com.gabodev.ecopacaanalyzer.utils.toFormattedDate

@Composable
fun ReadingItem(reading: Reading, onClick: () -> Unit) {
    val formattedDate = reading.timestamp.toLong().toFormattedDate()
    Column(modifier = Modifier
        .padding(bottom = 16.dp)
        .clickable { onClick() }) {
        Text("Fecha: $formattedDate")
        Text("Humedad: ${reading.humidity}")
        Text("Presión: ${reading.pressure}")
        Text("Temperatura: ${reading.temperature}")
    }
}