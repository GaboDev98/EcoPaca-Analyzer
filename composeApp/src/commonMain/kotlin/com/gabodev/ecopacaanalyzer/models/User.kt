package com.gabodev.ecopacaanalyzer.models

data class User(
    val id: String = "",
    val name: String? = "",
    val readings: Map<String, Reading> = emptyMap()
)