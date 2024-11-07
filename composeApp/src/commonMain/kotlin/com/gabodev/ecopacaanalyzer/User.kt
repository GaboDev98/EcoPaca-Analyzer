package com.gabodev.ecopacaanalyzer

data class User(
    val id: String,
    val readings: Map<String, Reading>
)