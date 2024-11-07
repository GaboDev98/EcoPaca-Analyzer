package com.gabodev.ecopacaanalyzer.models

data class User(
    val id: String,
    val readings: Map<String, Reading>
)