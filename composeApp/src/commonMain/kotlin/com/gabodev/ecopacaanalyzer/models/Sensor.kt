package com.gabodev.ecopacaanalyzer.models

data class Sensor(
    val name: String,
    val value: String,
    val unit: String,
    val type: String,
    val icon: String = ""
)
