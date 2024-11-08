package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gabodev.ecopacaanalyzer.models.Device
import com.gabodev.ecopacaanalyzer.viewmodel.PacaViewModel

@Composable
fun DeviceListScreen(
    viewModel: PacaViewModel,
    onDeviceClick: (String) -> Unit
) {
    val devicesState = viewModel.devices.collectAsState(initial = emptyList())
    val isLoading = viewModel.isLoading.collectAsState().value
    val error = viewModel.error.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Toolbar(title = "Dispositivos de las pacas", showBackButton = false, {})

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else if (error != null) {
                Text(
                    text = "Error al cargar las pacas: ${error.message}",
                    style = MaterialTheme.typography.body1,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
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
            Text(
                text = "Paca #$counter",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = device.id,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}