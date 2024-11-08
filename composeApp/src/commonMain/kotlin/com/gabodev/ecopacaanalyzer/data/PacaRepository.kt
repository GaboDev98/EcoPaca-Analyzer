package com.gabodev.ecopacaanalyzer.data

import com.gabodev.ecopacaanalyzer.models.Reading
import com.gabodev.ecopacaanalyzer.models.User
import kotlinx.coroutines.flow.MutableStateFlow

interface PacaRepository {

    suspend fun getReadingDetail(userId: String, readingId: String): Reading?

    fun listenForUsersUpdates(usersFlow: MutableStateFlow<List<User>>)

    fun listenForReadingsUpdates(userId: String, readingsFlow: MutableStateFlow<Map<String, Reading>>)
}