package com.gabodev.iotdevicemonitor.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gabodev.iotdevicemonitor.models.Device
import com.gabodev.iotdevicemonitor.ui.theme.Spacing

/** Organism: device identity, health and its two key live readings. The whole card opens the device. */
@Composable
fun DeviceCard(device: Device, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val latest = device.latestReading
    val status = device.status()
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Column(Modifier.padding(Spacing.lg), verticalArrangement = Arrangement.spacedBy(Spacing.lg)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                DeviceIconTile(device.type)
                Column(Modifier.weight(1f)) {
                    Text(
                        device.name?.takeIf { it.isNotBlank() } ?: device.type.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1, overflow = TextOverflow.Ellipsis,
                    )
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        Icon(Icons.Rounded.LocationOn, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            device.location?.takeIf { it.isNotBlank() } ?: device.type.displayName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1, overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                StatusBadge(status)
            }
            if (latest != null) {
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    MetricTile(Metric.Temperature, latest.valueOf(Metric.Temperature), Modifier.weight(1f), compactLabel = true)
                    MetricTile(Metric.Humidity, latest.valueOf(Metric.Humidity), Modifier.weight(1f), compactLabel = true)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    if (latest == null) "Sin lecturas todavía" else "Actualizado ${relativeTime(latest.epochSeconds)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
