package com.gabodev.ecopacaanalyzer.data

import com.gabodev.ecopacaanalyzer.models.Reading
import com.gabodev.ecopacaanalyzer.models.User

interface PacaRepository {

    suspend fun getUsers(): List<User>

    suspend fun getReadings(userId: String): Map<String, Reading>

    suspend fun getReadingDetail(userId: String, readingId: String): Reading?
}