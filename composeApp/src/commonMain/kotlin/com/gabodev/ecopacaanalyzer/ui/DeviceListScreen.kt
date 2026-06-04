package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gabodev.ecopacaanalyzer.models.Device
import com.gabodev.ecopacaanalyzer.viewmodel.PacaViewModel

@Composable
fun DeviceListScreen(
    viewModel: PacaViewModel,
    onDeviceClick: (String) -> Unit,
    onLogout: () -> Unit = {}
) {
    val devicesState = viewModel.devices.collectAsState(initial = emptyList())
    val isLoading = viewModel.isLoading.collectAsState().value
    val error = viewModel.error.collectAsState().value
    val successMessage = viewModel.successMessage.collectAsState().value
    var showAddDeviceDialog by remember { mutableStateOf(false) }

    LaunchedEffect(successMessage) {
        if (successMessage != null) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(error) {
        if (error != null) {
            kotlinx.coroutines.delay(4000)
            viewModel.clearMessages()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Toolbar(title = "Dispositivos", showBackButton = false, onLogoutClick = { onLogout() })

            AnimatedVisibility(visible = successMessage != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    backgroundColor = Color(0xC8E6C9),
                    shape = RoundedCornerShape(8.dp),
                    elevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("✓", color = Color(0x2E7D32), fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
                        Text(
                            successMessage ?: "",
                            style = MaterialTheme.typography.body2,
                            color = Color(0x1B5E20)
                        )
                    }
                }
            }

            AnimatedVisibility(visible = error != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    backgroundColor = Color(0xFFCDD2D7).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp),
                    elevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("✗", color = Color.Red, fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
                        Text(
                            error?.message ?: "Error desconocido",
                            style = MaterialTheme.typography.body2,
                            color = Color(0xB71C1C)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.White)
                    .weight(1f)
            ) {
            if (isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Cargando dispositivos...", style = MaterialTheme.typography.body1)
                }
            } else if (error != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "❌",
                        fontSize = MaterialTheme.typography.h3.fontSize
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Error al cargar",
                        style = MaterialTheme.typography.h6
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error.message ?: "Error desconocido",
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray
                    )
                }
            } else if (devicesState.value.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📱",
                        fontSize = MaterialTheme.typography.h2.fontSize
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Sin dispositivos",
                        style = MaterialTheme.typography.h6
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No hay dispositivos registrados.\nIntenta conectar un nuevo dispositivo.",
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    var counter = 0
                    items(devicesState.value) { device ->
                        counter++
                        DeviceItem(device, counter) {
                            onDeviceClick(device.id)
                        }
                    }
                }
            }
            }
        }

        FloatingActionButton(
            onClick = { showAddDeviceDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Text("+", fontSize = 24.sp, color = Color.White)
        }
    }

    if (showAddDeviceDialog) {
        AddDeviceDialog(
            onDismiss = { showAddDeviceDialog = false },
            onAddDevice = { deviceId, deviceName, deviceType ->
                viewModel.registerDevice(deviceId, deviceName, deviceType.name)
                showAddDeviceDialog = false
            }
        )
    }
}

@Composable
fun DeviceItem(device: Device, counter: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = device.name ?: "Dispositivo #$counter",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = device.type.displayName,
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    device.location?.let {
                        Text(
                            text = "📍 $it",
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
                DeviceIcon(device.type)
            }

            device.readings.values.maxByOrNull { it.timestamp.toLongOrNull() ?: 0 }?.let { lastReading ->
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                if (lastReading.sensors.isNotEmpty()) {
                    Text(
                        text = "Última lectura:",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    SensorDisplay(lastReading.sensors, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}