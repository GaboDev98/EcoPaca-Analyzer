package com.gabodev.iotdevicemonitor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gabodev.iotdevicemonitor.models.Reading
import com.gabodev.iotdevicemonitor.ui.components.DeviceIconTile
import com.gabodev.iotdevicemonitor.ui.components.EmptyState
import com.gabodev.iotdevicemonitor.ui.components.Metric
import com.gabodev.iotdevicemonitor.ui.components.ReadingCard
import com.gabodev.iotdevicemonitor.ui.components.StatusBadge
import com.gabodev.iotdevicemonitor.ui.components.epochSeconds
import com.gabodev.iotdevicemonitor.ui.components.formatMetric
import com.gabodev.iotdevicemonitor.ui.components.relativeTime
import com.gabodev.iotdevicemonitor.ui.components.status
import com.gabodev.iotdevicemonitor.ui.components.valueOf
import com.gabodev.iotdevicemonitor.ui.theme.ReadingStyles
import com.gabodev.iotdevicemonitor.ui.theme.Spacing
import com.gabodev.iotdevicemonitor.viewmodel.PacaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingsScreen(viewModel: PacaViewModel, deviceId: String, navController: NavHostController) {
    val readings by viewModel.readings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val devices by viewModel.devices.collectAsState()
    val device = devices.firstOrNull { it.id == deviceId }

    LaunchedEffect(deviceId) { viewModel.loadReadings(deviceId) }

    val sorted = readings.values.sortedByDescending { it.epochSeconds ?: Long.MIN_VALUE }
    val latest = sorted.firstOrNull()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(device?.name ?: "Dispositivo", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Volver")
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        when {
            isLoading && sorted.isEmpty() -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            latest == null -> EmptyState(
                icon = Icons.Rounded.Schedule,
                title = "Sin lecturas todavía",
                message = "Cuando el dispositivo envíe datos aparecerán aquí en tiempo real.",
                modifier = Modifier.padding(padding),
            )
            else -> LazyColumn(
                contentPadding = PaddingValues(start = Spacing.lg, end = Spacing.lg, top = padding.calculateTopPadding() + Spacing.sm, bottom = padding.calculateBottomPadding() + Spacing.xxl),
                verticalArrangement = Arrangement.spacedBy(Spacing.md),
                modifier = Modifier.fillMaxSize(),
            ) {
                item(key = "hero") { LatestReadingHero(latest, device) }
                if (sorted.size > 1) {
                    item(key = "history") {
                        Text("Historial", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = Spacing.md))
                    }
                    items(sorted.drop(1), key = { it.timestamp }) { ReadingCard(it) }
                }
            }
        }
    }
}

@Composable
private fun LatestReadingHero(reading: Reading, device: com.gabodev.iotdevicemonitor.models.Device?) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(Spacing.lg), verticalArrangement = Arrangement.spacedBy(Spacing.lg)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                device?.let { DeviceIconTile(it.type) }
                Column(Modifier.weight(1f)) {
                    Text("Última lectura", style = MaterialTheme.typography.titleSmall)
                    Text(
                        relativeTime(reading.epochSeconds),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                device?.let { StatusBadge(it.status()) }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.lg)) {
                Metric.entries.forEach { metric ->
                    Column(Modifier.weight(1f)) {
                        Text(metric.label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(formatMetric(metric, reading.valueOf(metric)), style = ReadingStyles.large, maxLines = 1)
                        Text(metric.unit, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
