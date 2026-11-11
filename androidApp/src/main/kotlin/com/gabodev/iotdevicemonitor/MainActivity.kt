package com.gabodev.iotdevicemonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gabodev.iotdevicemonitor.data.FirebasePacaRepository
import com.gabodev.iotdevicemonitor.viewmodel.AuthViewModel
import com.gabodev.iotdevicemonitor.viewmodel.PacaViewModel

class MainActivity : ComponentActivity() {
    private val pacaViewModel by lazy { PacaViewModel(FirebasePacaRepository()) }
    private val authViewModel by lazy { AuthViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(pacaViewModel, authViewModel)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val pacaViewModel by lazy { PacaViewModel(FirebasePacaRepository()) }
    val authViewModel by lazy { AuthViewModel() }
    App(pacaViewModel, authViewModel)
}