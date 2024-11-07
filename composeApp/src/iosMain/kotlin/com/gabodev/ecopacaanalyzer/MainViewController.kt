package com.gabodev.ecopacaanalyzer

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    val viewModel: PacaViewModel = viewModel()
    App(viewModel = viewModel)
}
