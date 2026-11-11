package com.gabodev.iotdevicemonitor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Compress
import androidx.compose.material.icons.rounded.DeviceThermostat
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.gabodev.iotdevicemonitor.models.Reading
import com.gabodev.iotdevicemonitor.ui.theme.IotTheme
import com.gabodev.iotdevicemonitor.ui.theme.ReadingStyles
import com.gabodev.iotdevicemonitor.ui.theme.Spacing
import kotlin.math.round

enum class Metric(val label: String, val shortLabel: String, val unit: String, val icon: ImageVector) {
    Temperature("Temperatura", "Temp.", "°C", Icons.Rounded.DeviceThermostat),
    Humidity("Humedad", "Humedad", "%", Icons.Rounded.WaterDrop),
    Pressure("Presión", "Presión", "hPa", Icons.Rounded.Compress),
}

/** Raw reading value for a metric, or null if missing/blank. */
fun Reading.valueOf(metric: Metric): String? = when (metric) {
    Metric.Temperature -> temperature
    Metric.Humidity -> humidity
    Metric.Pressure -> pressure
}?.takeIf { it.isNotBlank() }

/** Temperature 1 decimal, humidity and pressure integers; missing → "—". */
fun formatMetric(metric: Metric, raw: String?): String {
    val v = raw?.toDoubleOrNull() ?: return raw ?: "—"
    return when (metric) {
        Metric.Temperature -> (round(v * 10) / 10).toString()
        Metric.Humidity, Metric.Pressure -> round(v).toLong().toString()
    }
}

@Composable
private fun Metric.tint(): Color = when (this) {
    Metric.Temperature -> IotTheme.colors.sensorTemperature
    Metric.Humidity -> IotTheme.colors.sensorHumidity
    Metric.Pressure -> IotTheme.colors.sensorPressure
}

/** Molecule: one sensor value with icon, label and unit. [alert] switches to the warning container. */
@Composable
fun MetricTile(
    metric: Metric,
    rawValue: String?,
    modifier: Modifier = Modifier,
    alert: Boolean = false,
    compactLabel: Boolean = false,
) {
    val scheme = MaterialTheme.colorScheme
    val iot = IotTheme.colors
    val container = if (alert) iot.warningContainer else scheme.surfaceContainerHighest
    val content = if (alert) iot.onWarningContainer else scheme.onSurface
    val muted = if (alert) iot.onWarningContainer else scheme.onSurfaceVariant
    Column(
        modifier = modifier
            .background(container, MaterialTheme.shapes.medium)
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Icon(metric.icon, contentDescription = null, tint = if (alert) iot.onWarningContainer else metric.tint(), modifier = Modifier.size(18.dp))
            Text(if (compactLabel) metric.shortLabel else metric.label, style = MaterialTheme.typography.labelMedium, color = muted)
        }
        val value = formatMetric(metric, rawValue)
        Text(
            buildAnnotatedString {
                append(value)
                if (value != "—") withStyle(SpanStyle(fontSize = MaterialTheme.typography.labelSmall.fontSize, color = muted)) {
                    append(" ")
                    append(metric.unit)
                }
            },
            style = ReadingStyles.medium,
            color = content,
        )
    }
}
