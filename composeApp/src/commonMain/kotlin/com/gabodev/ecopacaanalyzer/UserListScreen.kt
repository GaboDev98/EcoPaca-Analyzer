package com.gabodev.ecopacaanalyzer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserListScreen(viewModel: PacaViewModel, onUserClick: (String) -> Unit) {
    val users = viewModel.users.collectAsState().value

    LaunchedEffect(true) {
        viewModel.loadUsers()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Dispositivos de las Pacas", modifier = Modifier.padding(bottom = 16.dp))

        users.forEach { user ->
            Button(onClick = { onUserClick(user.id) }) {
                Text(text = user.id)
            }
        }
    }
}