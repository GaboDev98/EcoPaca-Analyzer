package com.gabodev.iotdevicemonitor.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gabodev.iotdevicemonitor.ui.theme.IotTheme
import com.gabodev.iotdevicemonitor.ui.theme.Spacing

enum class BannerTone { Error, Success, Warning, Info }

/** Molecule: inline message that says what happened and what to do next. */
@Composable
fun AlertBanner(message: String, tone: BannerTone, modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    val iot = IotTheme.colors
    val (container, content, icon) = when (tone) {
        BannerTone.Error -> Triple(scheme.errorContainer, scheme.onErrorContainer, Icons.Rounded.Error)
        BannerTone.Success -> Triple(iot.successContainer, iot.onSuccessContainer, Icons.Rounded.CheckCircle)
        BannerTone.Warning -> Triple(iot.warningContainer, iot.onWarningContainer, Icons.Rounded.Warning)
        BannerTone.Info -> Triple(scheme.secondaryContainer, scheme.onSecondaryContainer, Icons.Rounded.Info)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(container, MaterialTheme.shapes.small)
            .padding(horizontal = Spacing.lg, vertical = Spacing.md),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(icon, contentDescription = null, tint = content, modifier = Modifier.size(20.dp))
        Text(message, style = MaterialTheme.typography.bodyMedium, color = content, modifier = Modifier.weight(1f))
    }
}

/** Animated banner that shows only while [message] is non-null. */
@Composable
fun AlertBannerSlot(message: String?, tone: BannerTone, modifier: Modifier = Modifier) {
    // Keep the last text so the exit animation doesn't render an empty banner.
    var shown by remember { mutableStateOf(message.orEmpty()) }
    if (message != null) shown = message
    AnimatedVisibility(visible = message != null, modifier = modifier) {
        AlertBanner(shown, tone)
    }
}
