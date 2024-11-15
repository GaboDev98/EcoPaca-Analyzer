package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.animation.animateColorAsState
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
import com.gabodev.ecopacaanalyzer.viewmodel.PacaViewModel
import androidx.compose.animation.core.*
import com.gabodev.ecopacaanalyzer.models.Reading

@Composable
fun ReadingsScreen(viewModel: PacaViewModel, deviceId: String, navController: NavHostController) {
    val readings = viewModel.readings.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    val lazyListState = rememberLazyListState()

    LaunchedEffect(deviceId) {
        viewModel.loadReadings(deviceId)
    }

    LaunchedEffect(readings) {
        lazyListState.scrollToItem(0)
    }

    var lastReading by remember { mutableStateOf<Reading?>(null) }

    val colorTransition = animateColorAsState(
        targetValue = if (lastReading != null) Color.Yellow else Color.Transparent,
        animationSpec = tween(durationMillis = 500)
    )

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

                    val previousReading = sortedReadings.firstOrNull()

                    previousReading?.let { reading ->
                        if (lastReading != reading) {
                            lastReading = reading
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                                .background(colorTransition.value),
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