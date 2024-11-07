package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gabodev.ecopacaanalyzer.utils.toFormattedDate
import com.gabodev.ecopacaanalyzer.viewmodel.PacaViewModel

@Composable
fun ReadingDetailScreen(
    viewModel: PacaViewModel,
    userId: String,
    readingId: String,
    navController: NavController,
) {
    val reading = viewModel.readingDetail.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    LaunchedEffect(userId, readingId) {
        viewModel.getReadingDetail(userId, readingId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Toolbar(title = "Detalle de la lectura", showBackButton = true, onBackClick = {
            navController.popBackStack()
        })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .shadow(8.dp, shape = RoundedCornerShape(16.dp)),
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        reading?.let {
                            val formattedDate = it.timestamp.toLong().toFormattedDate()
                            Text(
                                text = "Fecha y hora: $formattedDate",
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Temperatura: ${it.temperature} °C",
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Humedad: ${it.humidity}%",
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Presión: ${it.pressure} hPa",
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        } ?: run {
                            Text(
                                text = "Datos no disponibles",
                                style = MaterialTheme.typography.body2.copy(color = Color.Gray),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}