package com.gabodev.ecopacaanalyzer.data

import com.gabodev.ecopacaanalyzer.models.Reading
import com.gabodev.ecopacaanalyzer.models.Device
import kotlinx.coroutines.flow.MutableStateFlow

interface PacaRepository {

    suspend fun getReadingDetail(deviceId: String, readingId: String): Reading?

    fun listenForDevicesUpdates(devicesFlow: MutableStateFlow<List<Device>>)

    fun listenForReadingsUpdates(deviceId: String, readingsFlow: MutableStateFlow<Map<String, Reading>>)
}