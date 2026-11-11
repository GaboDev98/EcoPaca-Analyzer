package com.gabodev.iotdevicemonitor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform