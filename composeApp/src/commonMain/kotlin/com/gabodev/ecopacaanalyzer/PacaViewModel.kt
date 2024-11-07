package com.gabodev.ecopacaanalyzer

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

    private val _pacaReadings = MutableStateFlow<List<PacaReading>>(emptyList())
    val pacaReadings: StateFlow<List<PacaReading>> = _pacaReadings

    init {
        loadReadings()
    }

    private fun loadReadings() {
        CoroutineScope(Dispatchers.Main).launch {
            _pacaReadings.value = repository.getPacaReadings()
        }
    }

    fun loadUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val usersFromFirebase = repository.getUsers()
                _users.value = usersFromFirebase
            } catch (e: Exception) {
            }
        }
    }
    
    fun loadReadings(userId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val readingsFromFirebase = repository.getReadings(userId)
                _readings.value = readingsFromFirebase
            } catch (e: Exception) {
            }
        }
    }
}