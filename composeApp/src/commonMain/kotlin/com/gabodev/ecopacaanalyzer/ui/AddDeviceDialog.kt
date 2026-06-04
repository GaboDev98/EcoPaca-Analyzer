package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gabodev.ecopacaanalyzer.models.DeviceType

@Composable
fun AddDeviceDialog(
    onDismiss: () -> Unit,
    onAddDevice: (deviceId: String, deviceName: String, deviceType: DeviceType) -> Unit
) {
    var deviceId by remember { mutableStateOf("") }
    var deviceName by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(DeviceType.BIODIGESTER_BALE) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(12.dp),
        title = {
            Text(
                "Agregar dispositivo",
                style = MaterialTheme.typography.h6
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                OutlinedTextField(
                    value = deviceId,
                    onValueChange = { deviceId = it },
                    label = { Text("ID del dispositivo") },
                    placeholder = { Text("Ej: 5467f567b4fc") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = deviceName,
                    onValueChange = { deviceName = it },
                    label = { Text("Nombre") },
                    placeholder = { Text("Ej: Paca #1, Filtro Aire 1") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Text(
                    "Tipo de dispositivo:",
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.padding(top = 12.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DeviceType.values().forEach { type ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedType = type },
                            elevation = 0.dp,
                            backgroundColor = if (selectedType == type)
                                MaterialTheme.colors.primary.copy(alpha = 0.15f)
                            else
                                Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedType == type,
                                    onClick = { selectedType = type },
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "${type.icon} ${type.displayName}",
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                                if (selectedType == type) {
                                    Text(
                                        "✓",
                                        color = MaterialTheme.colors.primary,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (deviceId.isNotEmpty() && deviceName.isNotEmpty()) {
                        onAddDevice(deviceId, deviceName, selectedType)
                        onDismiss()
                    }
                },
                enabled = deviceId.isNotEmpty() && deviceName.isNotEmpty(),
                modifier = Modifier.padding(end = 8.dp),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("Agregar", modifier = Modifier.padding(horizontal = 16.dp))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Cancelar")
            }
        }
    )
}
