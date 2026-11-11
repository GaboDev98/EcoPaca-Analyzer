package com.gabodev.iotdevicemonitor

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "IoT Device Monitor",
    ) {
        App()
    }
}