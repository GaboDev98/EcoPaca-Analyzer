package com.gabodev.iotdevicemonitor

import com.gabodev.iotdevicemonitor.data.PacaReading
import com.gabodev.iotdevicemonitor.data.PacaRepository

expect class FirebasePacaRepository : PacaRepository {
    override suspend fun getPacaReadings(): List<PacaReading>
}