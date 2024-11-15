package com.gabodev.ecopacaanalyzer.viewmodel

import com.gabodev.ecopacaanalyzer.models.Reading
import com.gabodev.ecopacaanalyzer.data.PacaRepository
import com.gabodev.ecopacaanalyzer.models.Device
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PacaViewModel(private val repository: PacaRepository) : ViewModel() {

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices

    private val _readings = MutableStateFlow<Map<String, Reading>>(emptyMap())
    val readings: StateFlow<Map<String, Reading>> = _readings

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<Exception?>(null)
    val error: StateFlow<Exception?> = _error

    init {
        listenForDevicesUpdates()
    }

    fun loadReadings(deviceId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.listenForReadingsUpdates(deviceId, _readings)
            } catch (e: Exception) {
                _error.value = e
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun listenForDevicesUpdates() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.listenForDevicesUpdates(_devices)
            } catch (e: Exception) {
                _error.value = e
            } finally {
                _isLoading.value = false
            }
        }
    }
}