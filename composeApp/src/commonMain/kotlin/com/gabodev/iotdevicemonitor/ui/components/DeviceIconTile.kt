package com.gabodev.iotdevicemonitor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.Compost
import androidx.compose.material.icons.rounded.DeviceThermostat
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.gabodev.iotdevicemonitor.models.DeviceType
import com.gabodev.iotdevicemonitor.ui.theme.IotTheme

val DeviceType.iconVector: ImageVector
    get() = when (this) {
        DeviceType.BIODIGESTER_BALE -> Icons.Rounded.Compost
        DeviceType.AIR_QUALITY_FILTER -> Icons.Rounded.Air
        DeviceType.TEMPERATURE_SENSOR -> Icons.Rounded.DeviceThermostat
        DeviceType.HUMIDITY_SENSOR -> Icons.Rounded.WaterDrop
    }

/** Atom: rounded tile that identifies a device category (replaces the old emoji icons). */
@Composable
fun DeviceIconTile(type: DeviceType, modifier: Modifier = Modifier, compact: Boolean = false) {
    val iot = IotTheme.colors
    val scheme = MaterialTheme.colorScheme
    val (container, content) = when (type) {
        DeviceType.BIODIGESTER_BALE -> scheme.primaryContainer to scheme.onPrimaryContainer
        DeviceType.AIR_QUALITY_FILTER -> iot.successContainer to iot.onSuccessContainer
        DeviceType.TEMPERATURE_SENSOR -> iot.sensorTemperatureContainer to iot.sensorTemperature
        DeviceType.HUMIDITY_SENSOR -> iot.sensorHumidityContainer to iot.sensorHumidity
    }
    Box(
        modifier = modifier
            .size(if (compact) 40.dp else 48.dp)
            .background(container, if (compact) MaterialTheme.shapes.medium else MaterialTheme.shapes.large),
        contentAlignment = Alignment.Center,
    ) {
        Icon(type.iconVector, contentDescription = type.displayName, tint = content)
    }
}
