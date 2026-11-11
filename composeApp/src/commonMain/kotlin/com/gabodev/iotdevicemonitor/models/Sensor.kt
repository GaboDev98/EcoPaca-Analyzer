package com.gabodev.iotdevicemonitor.models

data class Sensor(
    val name: String,
    val value: String,
    val unit: String,
    val type: String,
    val icon: String = ""
)
