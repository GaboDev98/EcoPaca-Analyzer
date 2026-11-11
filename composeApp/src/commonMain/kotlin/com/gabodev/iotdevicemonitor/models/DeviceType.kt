package com.gabodev.iotdevicemonitor.models

/** Stored in Firebase by [name]; the icon lives in the UI layer (DeviceIconTile). */
enum class DeviceType(val displayName: String) {
    BIODIGESTER_BALE("Biodigestor"),
    AIR_QUALITY_FILTER("Calidad de aire"),
    TEMPERATURE_SENSOR("Temperatura"),
    HUMIDITY_SENSOR("Humedad")
}
