package com.gabodev.iotdevicemonitor

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.window.ComposeUIViewController
import com.gabodev.iotdevicemonitor.viewmodel.PacaViewModel

fun MainViewController() = ComposeUIViewController {
    val viewModel: PacaViewModel = viewModel()
    App(viewModel = viewModel)
}
