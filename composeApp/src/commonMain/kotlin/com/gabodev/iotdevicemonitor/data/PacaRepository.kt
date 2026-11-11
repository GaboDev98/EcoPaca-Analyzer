package com.gabodev.iotdevicemonitor.data

import com.gabodev.iotdevicemonitor.models.Reading
import com.gabodev.iotdevicemonitor.models.Device
import kotlinx.coroutines.flow.MutableStateFlow

interface PacaRepository {

    suspend fun getReadingDetail(deviceId: String, readingId: String): Reading?

    suspend fun registerDevice(device: Device): Boolean

    fun listenForDevicesUpdates(devicesFlow: MutableStateFlow<List<Device>>)

    fun listenForReadingsUpdates(deviceId: String, readingsFlow: MutableStateFlow<Map<String, Reading>>)
}