package com.gabodev.iotdevicemonitor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gabodev.iotdevicemonitor.models.DeviceType
import com.gabodev.iotdevicemonitor.ui.components.iconVector
import com.gabodev.iotdevicemonitor.ui.theme.Spacing

/** Organism: modal form to register a device by ID, name, location and type. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddDeviceDialog(
    onDismiss: () -> Unit,
    onAddDevice: (deviceId: String, name: String, location: String, type: DeviceType) -> Unit,
) {
    var deviceId by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var type by rememberSaveable { mutableStateOf(DeviceType.BIODIGESTER_BALE) }
    val canConfirm = deviceId.isNotBlank() && name.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Rounded.AddCircleOutline, contentDescription = null) },
        title = { Text("Agregar dispositivo") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
                Text(
                    "Ingresa el ID que aparece en la etiqueta del dispositivo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                OutlinedTextField(deviceId, { deviceId = it.trim() }, label = { Text("ID del dispositivo") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(name, { name = it }, label = { Text("Nombre") }, placeholder = { Text("Ej. Paca norte #3") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(location, { location = it }, label = { Text("Ubicación (opcional)") }, placeholder = { Text("Ej. Invernadero A") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Text("Tipo", style = MaterialTheme.typography.titleSmall)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    DeviceType.entries.forEach { option ->
                        val selected = option == type
                        FilterChip(
                            selected = selected,
                            onClick = { type = option },
                            label = { Text(option.displayName) },
                            leadingIcon = {
                                Icon(
                                    if (selected) Icons.Rounded.Check else option.iconVector,
                                    contentDescription = null,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                                )
                            },
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onAddDevice(deviceId, name.trim(), location.trim(), type) }, enabled = canConfirm) {
                Text("Agregar")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}
