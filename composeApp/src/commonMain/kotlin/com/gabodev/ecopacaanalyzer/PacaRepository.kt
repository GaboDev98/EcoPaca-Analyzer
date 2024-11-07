package com.gabodev.ecopacaanalyzer

interface PacaRepository {
    suspend fun getPacaReadings(): List<PacaReading>

    suspend fun getUsers(): List<User>

    suspend fun getReadings(userId: String): Map<String, Reading>
}