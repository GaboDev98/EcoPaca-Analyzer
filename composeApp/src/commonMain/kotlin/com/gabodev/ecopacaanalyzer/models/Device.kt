package com.gabodev.ecopacaanalyzer.models

data class Device(
    val id: String = "",
    val name: String? = "",
    val type: DeviceType = DeviceType.BIODIGESTER_BALE,
    val location: String? = null,
    val readings: Map<String, Reading> = emptyMap()
)