package com.gabodev.ecopacaanalyzer

import com.gabodev.ecopacaanalyzer.data.PacaReading
import com.gabodev.ecopacaanalyzer.data.PacaRepository

expect class FirebasePacaRepository : PacaRepository {
    override suspend fun getPacaReadings(): List<PacaReading>
}