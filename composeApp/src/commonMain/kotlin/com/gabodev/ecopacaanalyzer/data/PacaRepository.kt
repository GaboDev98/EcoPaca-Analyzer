package com.gabodev.ecopacaanalyzer

interface PacaRepository {

    suspend fun getUsers(): List<User>

    suspend fun getReadings(userId: String): Map<String, Reading>

    suspend fun getReadingDetail(userId: String, readingId: String): Reading?
}