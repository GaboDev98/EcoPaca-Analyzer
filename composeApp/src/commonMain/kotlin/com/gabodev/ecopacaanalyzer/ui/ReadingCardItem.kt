package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            val formattedDate = reading.timestamp.toLongOrNull()?.toFormattedDate() ?: reading.timestamp
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Fecha y hora: $formattedDate",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "▼",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .graphicsLayer(rotationZ = rotationAngle)
                        .clickable { showDetails = !showDetails }
                        .padding(8.dp)
                )
            }

            if (showDetails) {
                Spacer(modifier = Modifier.height(8.dp))
                if (reading.sensors.isNotEmpty()) {
                    SensorDisplay(reading.sensors)
                } else {
                    ReadingItem(reading, isPadding = isPadding, showDateLabel = showDateLabel)
                }
            }
        }
    }
}