package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gabodev.ecopacaanalyzer.viewmodel.PacaViewModel

@Composable
fun UserListScreen(viewModel: PacaViewModel, onUserClick: (String) -> Unit) {
    val users = viewModel.users.collectAsState().value
    val isLoading =
        viewModel.isLoading.collectAsState().value  // Assuming you have a loading state in your ViewModel

    LaunchedEffect(true) {
        viewModel.loadUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        Text(
            text = "Dispositivos de las pacas",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                users.forEach { user ->
                    Button(
                        onClick = { onUserClick(user.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(
                                MaterialTheme.colors.primary,
                                shape = MaterialTheme.shapes.medium
                            )
                    ) {
                        Text(
                            text = user.id,
                            style = TextStyle(color = Color.White, fontSize = 16.sp)
                        )
                    }
                }
            }
        }
    }
}