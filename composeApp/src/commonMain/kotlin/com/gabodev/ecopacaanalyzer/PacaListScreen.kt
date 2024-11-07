package com.gabodev.ecopacaanalyzer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PacaListScreen(viewModel: PacaViewModel, onPacaClick: (PacaReading) -> Unit) {
    val readings by viewModel.pacaReadings.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Listado de Lecturas", style = MaterialTheme.typography.h5)

        LazyColumn {
            items(readings) { reading ->
                PacaListItem(reading) {
                    onPacaClick(reading)
                }
            }
        }
    }
}