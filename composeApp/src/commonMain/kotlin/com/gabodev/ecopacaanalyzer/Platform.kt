package com.gabodev.ecopacaanalyzer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform