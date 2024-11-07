package com.gabodev.ecopacaanalyzer

import com.gabodev.ecopacaanalyzer.utils.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}