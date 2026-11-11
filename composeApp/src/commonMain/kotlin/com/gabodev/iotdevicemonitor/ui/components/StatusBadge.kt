package com.gabodev.iotdevicemonitor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gabodev.iotdevicemonitor.ui.theme.IotTheme
import com.gabodev.iotdevicemonitor.ui.theme.Spacing

/** Atom: status label with a dot. Offline uses a hollow dot so it reads without color. */
@Composable
fun StatusBadge(status: DeviceStatus, modifier: Modifier = Modifier, label: String = status.label) {
    val iot = IotTheme.colors
    val scheme = MaterialTheme.colorScheme
    val (container, content, dot) = when (status) {
        DeviceStatus.Online -> Triple(iot.successContainer, iot.onSuccessContainer, iot.success)
        DeviceStatus.Warning -> Triple(iot.warningContainer, iot.onWarningContainer, iot.warning)
        DeviceStatus.Critical -> Triple(scheme.errorContainer, scheme.onErrorContainer, scheme.error)
        DeviceStatus.Offline -> Triple(scheme.surfaceContainerHighest, scheme.onSurfaceVariant, scheme.outline)
    }
    Row(
        modifier = modifier
            .height(24.dp)
            .background(container, MaterialTheme.shapes.extraSmall)
            .padding(horizontal = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        val dotModifier = Modifier.size(8.dp)
        Box(
            if (status == DeviceStatus.Offline) dotModifier.border(2.dp, dot, CircleShape)
            else dotModifier.background(dot, CircleShape)
        )
        Text(label, style = MaterialTheme.typography.labelMedium, color = content)
    }
}
