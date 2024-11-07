package com.gabodev.ecopacaanalyzer

expect class FirebasePacaRepository : PacaRepository {
    override suspend fun getPacaReadings(): List<PacaReading>
}