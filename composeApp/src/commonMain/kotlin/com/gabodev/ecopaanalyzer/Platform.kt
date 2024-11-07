package com.gabodev.ecopaanalyzer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform