package com.gabodev.iotdevicemonitor.models

import com.gabodev.iotdevicemonitor.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}