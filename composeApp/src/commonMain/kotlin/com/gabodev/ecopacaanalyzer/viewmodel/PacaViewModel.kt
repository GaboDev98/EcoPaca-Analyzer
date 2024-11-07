package com.gabodev.ecopacaanalyzer.viewmodel

import com.gabodev.ecopacaanalyzer.models.Reading
import com.gabodev.ecopacaanalyzer.data.PacaRepository
import com.gabodev.ecopacaanalyzer.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PacaViewModel(private val repository: PacaRepository) {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _readings = MutableStateFlow<Map<String, Reading>>(emptyMap())
    val readings: StateFlow<Map<String, Reading>> = _readings

    private val _readingDetail = MutableStateFlow<Reading?>(null)
    val readingDetail: StateFlow<Reading?> = _readingDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadUsers() {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val usersFromFirebase = repository.getUsers()
                _users.value = usersFromFirebase
            } catch (e: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadReadings(userId: String) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val readingsFromFirebase = repository.getReadings(userId)
                _readings.value = readingsFromFirebase
            } catch (e: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun getReadingDetail(userId: String, readingId: String) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val readingDetailFromFirebase = repository.getReadingDetail(userId, readingId)
                _readingDetail.value = readingDetailFromFirebase
            } catch (e: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }
}