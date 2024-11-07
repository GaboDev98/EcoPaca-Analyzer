package com.gabodev.ecopacaanalyzer.models

data class Reading(
    val id: String? = "",
    val humidity: String,
    val pressure: String,
    val temperature: String,
    val timestamp: String
)