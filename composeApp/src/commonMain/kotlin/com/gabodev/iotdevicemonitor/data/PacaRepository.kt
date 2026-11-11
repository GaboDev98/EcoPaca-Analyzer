package com.gabodev.iotdevicemonitor.data

import com.gabodev.iotdevicemonitor.models.Reading
import com.gabodev.iotdevicemonitor.models.Device
import kotlinx.coroutines.flow.MutableStateFlow

enum class RegisterResult {
    Success,
    /** The ID already belongs to another account. */
    AlreadyClaimed,
    NotSignedIn,
    Error
}

/**
 * Devices live at /devices/{deviceId} with an `owner` uid. Each user only sees and
 * edits their own devices (enforced by database.rules.json).
 */
interface PacaRepository {

    suspend fun getReadingDetail(deviceId: String, readingId: String): Reading?

    suspend fun registerDevice(device: Device): RegisterResult

    /** Emits the signed-in user's devices; follows login/logout automatically. */
    fun listenForDevicesUpdates(devicesFlow: MutableStateFlow<List<Device>>)

    /** Replaces any previous readings listener. */
    fun listenForReadingsUpdates(deviceId: String, readingsFlow: MutableStateFlow<Map<String, Reading>>)
}
