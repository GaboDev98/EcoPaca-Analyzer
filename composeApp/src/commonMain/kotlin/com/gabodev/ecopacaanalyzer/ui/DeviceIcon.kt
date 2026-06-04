package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.gabodev.ecopacaanalyzer.models.DeviceType

@Composable
fun DeviceIcon(deviceType: DeviceType, modifier: Modifier = Modifier) {
    Text(
        text = deviceType.icon,
        fontSize = 24.sp,
        modifier = modifier
    )
}
