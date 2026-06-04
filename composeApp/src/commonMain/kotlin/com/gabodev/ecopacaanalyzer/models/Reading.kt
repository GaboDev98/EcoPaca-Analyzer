package com.gabodev.ecopacaanalyzer.models

data class Reading(
    val id: String? = "",
    val timestamp: String = "",
    val sensors: Map<String, Sensor> = emptyMap(),
    val humidity: String? = null,
    val pressure: String? = null,
    val temperature: String? = null
)