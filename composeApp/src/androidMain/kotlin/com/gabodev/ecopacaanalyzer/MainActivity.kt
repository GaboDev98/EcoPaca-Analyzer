package com.gabodev.ecopacaanalyzer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gabodev.ecopacaanalyzer.data.FirebasePacaRepository
import com.gabodev.ecopacaanalyzer.viewmodel.PacaViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by lazy { PacaViewModel(FirebasePacaRepository()) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(viewModel)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val viewModel by lazy { PacaViewModel(FirebasePacaRepository()) }
    App(viewModel)
}