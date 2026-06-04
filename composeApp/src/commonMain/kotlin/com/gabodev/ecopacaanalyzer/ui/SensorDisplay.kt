package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gabodev.ecopacaanalyzer.models.Sensor

@Composable
fun SensorDisplay(sensors: Map<String, Sensor>, modifier: Modifier = Modifier) {
    if (sensors.isEmpty()) return

    Column(modifier = modifier) {
        sensors.forEach { (_, sensor) ->
            SensorRow(sensor)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun SensorRow(sensor: Sensor, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${sensor.icon} ${sensor.name}:",
            style = MaterialTheme.typography.body2,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${sensor.value} ${sensor.unit}",
            style = MaterialTheme.typography.body2
        )
    }
}
