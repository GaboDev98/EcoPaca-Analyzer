package com.gabodev.ecopacaanalyzer

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "EcoPaca Analyzer",
    ) {
        App()
    }
}