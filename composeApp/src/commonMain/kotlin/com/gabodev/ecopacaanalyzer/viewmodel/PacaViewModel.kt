package com.gabodev.ecopacaanalyzer.viewmodel

import com.gabodev.ecopacaanalyzer.models.Reading
import com.gabodev.ecopacaanalyzer.data.PacaRepository
import com.gabodev.ecopacaanalyzer.models.User
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PacaViewModel(private val repository: PacaRepository) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _readings = MutableStateFlow<Map<String, Reading>>(emptyMap())
    val readings: StateFlow<Map<String, Reading>> = _readings

    private val _readingDetail = MutableStateFlow<Reading?>(null)
    val readingDetail: StateFlow<Reading?> = _readingDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<Exception?>(null)
    val error: StateFlow<Exception?> = _error

    init {
        listenForUsersUpdates()
    }

    fun loadReadings(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.listenForReadingsUpdates(userId, _readings)
            } catch (e: Exception) {
                _error.value = e
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getReadingDetail(userId: String, readingId: String) {
        performOperation {
            val readingDetailFromFirebase = repository.getReadingDetail(userId, readingId)
            _readingDetail.value = readingDetailFromFirebase
        }
    }

    private fun listenForUsersUpdates() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.listenForUsersUpdates(_users)
            } catch (e: Exception) {
                _error.value = e
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun performOperation(operation: suspend () -> Unit) {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                operation()
            } catch (e: Exception) {
                _error.value = e
            } finally {
                _isLoading.value = false
            }
        }
    }
}