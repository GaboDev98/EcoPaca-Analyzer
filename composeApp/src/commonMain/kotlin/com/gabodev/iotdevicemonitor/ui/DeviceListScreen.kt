package com.gabodev.iotdevicemonitor.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.FilterAltOff
import androidx.compose.material.icons.rounded.Sensors
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.gabodev.iotdevicemonitor.models.DeviceType
import com.gabodev.iotdevicemonitor.ui.components.AlertBanner
import com.gabodev.iotdevicemonitor.ui.components.BannerTone
import com.gabodev.iotdevicemonitor.ui.components.DeviceCard
import com.gabodev.iotdevicemonitor.ui.components.EmptyState
import com.gabodev.iotdevicemonitor.ui.theme.Spacing
import com.gabodev.iotdevicemonitor.viewmodel.PacaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceListScreen(
    viewModel: PacaViewModel,
    onDeviceClick: (String) -> Unit,
    onLogout: () -> Unit = {},
) {
    val devices by viewModel.devices.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()

    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var filter by rememberSaveable { mutableStateOf<DeviceType?>(null) }
    var menuOpen by remember { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    LaunchedEffect(successMessage) {
        successMessage?.let { snackbar.showSnackbar(it); viewModel.clearMessages() }
    }

    val visible = devices.filter { filter == null || it.type == filter }
        .sortedBy { it.name?.lowercase() ?: "" }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("Mis dispositivos") },
                actions = {
                    Box {
                        IconButton(onClick = { menuOpen = true }) {
                            Icon(Icons.Rounded.AccountCircle, contentDescription = "Cuenta")
                        }
                        DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                            DropdownMenuItem(
                                text = { Text("Cerrar sesión") },
                                leadingIcon = { Icon(Icons.AutoMirrored.Rounded.Logout, contentDescription = null) },
                                onClick = { menuOpen = false; onLogout() },
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            if (devices.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = { showAddDialog = true },
                    icon = { Icon(Icons.Rounded.Add, contentDescription = null) },
                    text = { Text("Agregar dispositivo") },
                    expanded = scrollBehavior.state.collapsedFraction < 0.5f,
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            error?.let {
                AlertBanner(
                    it.message ?: "No pudimos cargar tus dispositivos. Revisa tu conexión e inténtalo de nuevo.",
                    BannerTone.Error,
                    Modifier.padding(horizontal = Spacing.lg, vertical = Spacing.sm),
                )
            }
            when {
                isLoading && devices.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                devices.isEmpty() -> EmptyState(
                    icon = Icons.Rounded.Sensors,
                    title = "Aún no tienes dispositivos",
                    message = "Agrega tu primer sensor para ver sus lecturas en tiempo real.",
                ) {
                    Button(onClick = { showAddDialog = true }) {
                        Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.padding(end = Spacing.sm))
                        Text("Agregar dispositivo")
                    }
                }
                else -> {
                    TypeFilterRow(filter) { filter = it }
                    if (visible.isEmpty()) {
                        EmptyState(
                            icon = Icons.Rounded.FilterAltOff,
                            title = "Sin resultados",
                            message = "No tienes dispositivos de este tipo.",
                        ) { TextButton(onClick = { filter = null }) { Text("Quitar filtros") } }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(start = Spacing.lg, end = Spacing.lg, top = Spacing.sm, bottom = 96.dp),
                            verticalArrangement = Arrangement.spacedBy(Spacing.md),
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(visible, key = { it.id }) { device ->
                                DeviceCard(device, onClick = { onDeviceClick(device.id) })
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddDeviceDialog(
            onDismiss = { showAddDialog = false },
            onAddDevice = { id, name, location, type ->
                viewModel.registerDevice(id, name, type, location)
                showAddDialog = false
            },
        )
    }
}

@Composable
private fun TypeFilterRow(selected: DeviceType?, onSelect: (DeviceType?) -> Unit) {
    Row(
        Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = Spacing.lg, vertical = Spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        val chip: @Composable (String, Boolean, () -> Unit) -> Unit = { label, isSelected, onClick ->
            FilterChip(
                selected = isSelected,
                onClick = onClick,
                label = { Text(label) },
                leadingIcon = if (isSelected) ({ Icon(Icons.Rounded.Check, null, Modifier.size(FilterChipDefaults.IconSize)) }) else null,
            )
        }
        chip("Todos", selected == null) { onSelect(null) }
        DeviceType.entries.forEach { type -> chip(type.displayName, selected == type) { onSelect(type) } }
    }
}
