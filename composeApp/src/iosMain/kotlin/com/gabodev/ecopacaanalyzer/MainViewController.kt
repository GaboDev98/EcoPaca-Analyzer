package com.gabodev.ecopacaanalyzer

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.window.ComposeUIViewController
import com.gabodev.ecopacaanalyzer.viewmodel.PacaViewModel

fun MainViewController() = ComposeUIViewController {
    val viewModel: PacaViewModel = viewModel()
    App(viewModel = viewModel)
}
