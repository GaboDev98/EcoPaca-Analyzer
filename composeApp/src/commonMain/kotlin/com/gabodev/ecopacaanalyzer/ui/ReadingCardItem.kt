package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.gabodev.ecopacaanalyzer.models.Reading
import com.gabodev.ecopacaanalyzer.utils.toFormattedDate

@Composable
fun ReadingCardItem(reading: Reading, isPadding: Boolean = true, showDateLabel: Boolean = true, onClick: () -> Unit) {
    var showDetails by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (showDetails) 180f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

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
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = if (showDetails) "Ocultar detalles" else "Mostrar detalles",
                        modifier = Modifier.graphicsLayer(rotationZ = rotationAngle) // Aplicar la rotación animada
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