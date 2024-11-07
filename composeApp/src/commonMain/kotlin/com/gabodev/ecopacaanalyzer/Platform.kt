package com.gabodev.ecopacaanalyzer.utils

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform