package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gabodev.ecopacaanalyzer.utils.toFormattedDate
import com.gabodev.ecopacaanalyzer.viewmodel.PacaViewModel

@Composable
fun ReadingsScreen(viewModel: PacaViewModel, userId: String, navController: NavHostController) {
    val readings = viewModel.readings.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    val lazyListState = rememberLazyListState()

    LaunchedEffect(userId) {
        viewModel.loadReadings(userId)
    }

    LaunchedEffect(readings) {
        lazyListState.scrollToItem(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Toolbar(title = "Lecturas de la paca", showBackButton = true, onBackClick = {
            navController.popBackStack()
        })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                if (readings.isNotEmpty()) {
                    val sortedReadings = readings.values
                        .sortedByDescending { it.timestamp.toLongOrNull() ?: Long.MIN_VALUE }

                    val lastReading = sortedReadings.firstOrNull()

                    lastReading?.let { reading ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                                .background(MaterialTheme.colors.surface),
                            elevation = 4.dp
                        ) {
                            Column {
                                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp)) {
                                    Text("Última lectura:", style = MaterialTheme.typography.h6)
                                }
                                ReadingItem(reading, isPadding = true)
                            }
                        }
                    }

                    Text(
                        text = "Historial de lecturas",
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 4.dp, end = 4.dp, top = 8.dp, bottom = 8.dp)
                    )

                    LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                        items(sortedReadings.drop(1), key = { it -> (it.timestamp.takeIf { it.isNotEmpty() } ?: it.id)!! }) { reading ->
                            ReadingCardItem(reading, isPadding = false, showDateLabel = false) { }
                        }
                    }
                } else {
                    Text(
                        text = "Lecturas no disponibles.",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}