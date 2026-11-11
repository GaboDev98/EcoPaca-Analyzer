package com.gabodev.iotdevicemonitor.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gabodev.iotdevicemonitor.models.Reading
import com.gabodev.iotdevicemonitor.ui.theme.Spacing
import com.gabodev.iotdevicemonitor.utils.toFormattedDate

/** Organism: one timestamped reading with every sensor value. */
@Composable
fun ReadingCard(reading: Reading, modifier: Modifier = Modifier) {
    // toFormattedDate() → "dd/MM/yyyy HH:mm"
    val formatted = reading.epochSeconds?.toFormattedDate()
    val date = formatted?.substringBefore(' ') ?: reading.timestamp
    val time = formatted?.substringAfter(' ') ?: ""
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Column(Modifier.padding(Spacing.lg), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(time, style = MaterialTheme.typography.titleSmall)
                    Text(date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(relativeTime(reading.epochSeconds), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                Metric.entries.forEach { metric ->
                    MetricTile(metric, reading.valueOf(metric), Modifier.weight(1f), compactLabel = true)
                }
            }
        }
    }
}
