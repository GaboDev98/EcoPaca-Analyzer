package com.gabodev.ecopacaanalyzer.models

enum class DeviceType(
    val displayName: String,
    val icon: String
) {
    BIODIGESTER_BALE("Biodigestor", "🌾"),
    AIR_QUALITY_FILTER("Filtro de Aire", "🌬️"),
    TEMPERATURE_SENSOR("Temperatura", "🌡️"),
    HUMIDITY_SENSOR("Humedad", "💧")
}
