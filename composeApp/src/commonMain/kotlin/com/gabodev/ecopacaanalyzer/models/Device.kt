package com.gabodev.ecopacaanalyzer.models

data class Device(
    val id: String = "",
    val name: String? = "",
    val readings: Map<String, Reading> = emptyMap()
)