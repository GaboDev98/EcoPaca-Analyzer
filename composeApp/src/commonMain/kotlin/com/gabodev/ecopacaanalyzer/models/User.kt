package com.gabodev.ecopacaanalyzer.data

import com.gabodev.ecopacaanalyzer.Reading

data class User(
    val id: String,
    val readings: Map<String, Reading>
)