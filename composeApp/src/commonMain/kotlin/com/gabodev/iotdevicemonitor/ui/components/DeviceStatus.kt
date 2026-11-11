package com.gabodev.iotdevicemonitor.ui.components

import com.gabodev.iotdevicemonitor.models.Device
import com.gabodev.iotdevicemonitor.models.Reading
import com.gabodev.iotdevicemonitor.utils.currentEpochSeconds

/** Device health shown by [StatusBadge]. */
enum class DeviceStatus(val label: String) {
    Online("En línea"),
    Warning("Revisar"),
    Critical("Crítico"),
    Offline("Sin conexión"),
}

private const val ONLINE_WINDOW_S = 5 * 60L
private const val OFFLINE_AFTER_S = 30 * 60L

val Reading.epochSeconds: Long? get() = timestamp.toLongOrNull()

val Device.latestReading: Reading?
    get() = readings.values.maxByOrNull { it.epochSeconds ?: Long.MIN_VALUE }

/** Online if the last reading is under 5 min old, Revisar under 30 min, else Sin conexión. */
fun Device.status(now: Long = currentEpochSeconds()): DeviceStatus {
    val last = latestReading?.epochSeconds ?: return DeviceStatus.Offline
    val age = now - last
    return when {
        age <= ONLINE_WINDOW_S -> DeviceStatus.Online
        age <= OFFLINE_AFTER_S -> DeviceStatus.Warning
        else -> DeviceStatus.Offline
    }
}

/** "hace 2 min", "hace 3 h", "hace 4 d". */
fun relativeTime(epochSeconds: Long?, now: Long = currentEpochSeconds()): String {
    if (epochSeconds == null) return "Sin lecturas"
    val diff = (now - epochSeconds).coerceAtLeast(0)
    return when {
        diff < 60 -> "hace un momento"
        diff < 3600 -> "hace ${diff / 60} min"
        diff < 86_400 -> "hace ${diff / 3600} h"
        else -> "hace ${diff / 86_400} d"
    }
}
