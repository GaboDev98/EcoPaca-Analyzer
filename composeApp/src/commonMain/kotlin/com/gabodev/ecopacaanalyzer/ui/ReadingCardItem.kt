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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

@Composable
fun ReadingCardItem(reading: Reading, isPadding: Boolean = true, showDateLabel: Boolean = true, onClick: () -> Unit) {
    var showDetails by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val formattedDate = reading.timestamp.toLong().toFormattedDate()
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Fecha y hora: $formattedDate",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { showDetails = !showDetails },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = if (showDetails) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropDown,
                        contentDescription = if (showDetails) "Ocultar detalles" else "Mostrar detalles"
                    )
                }
            }

            if (showDetails) {
                Spacer(modifier = Modifier.height(8.dp))
                ReadingItem(reading, isPadding = isPadding, showDateLabel = showDateLabel)
            }
        }
    }
}