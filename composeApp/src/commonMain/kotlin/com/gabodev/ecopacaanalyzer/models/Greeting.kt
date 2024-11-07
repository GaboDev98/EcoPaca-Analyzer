package com.gabodev.ecopacaanalyzer.models

import com.gabodev.ecopacaanalyzer.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}