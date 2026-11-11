package com.gabodev.iotdevicemonitor.viewmodel

import com.gabodev.iotdevicemonitor.models.Reading
import com.gabodev.iotdevicemonitor.data.PacaRepository
import com.gabodev.iotdevicemonitor.data.RegisterResult
import com.gabodev.iotdevicemonitor.models.Device
import com.gabodev.iotdevicemonitor.models.DeviceType
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

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    init {
        listenForDevicesUpdates()
    }

    fun clearMessages() {
        _error.value = null
        _successMessage.value = null
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

    fun registerDevice(deviceId: String, deviceName: String, deviceType: DeviceType, location: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val device = Device(
                    id = deviceId,
                    name = deviceName,
                    type = deviceType,
                    location = location?.takeIf { it.isNotBlank() },
                    readings = emptyMap()
                )
                when (repository.registerDevice(device)) {
                    RegisterResult.Success -> {
                        _successMessage.value = "Dispositivo agregado"
                        _error.value = null
                    }
                    RegisterResult.AlreadyClaimed ->
                        _error.value = Exception("El ID $deviceId ya está registrado en otra cuenta.")
                    RegisterResult.NotSignedIn ->
                        _error.value = Exception("Tu sesión expiró. Vuelve a iniciar sesión.")
                    RegisterResult.Error ->
                        _error.value = Exception("No pudimos agregar el dispositivo. Verifica tu conexión.")
                }
            } catch (e: Exception) {
                _error.value = Exception("Error: ${e.message ?: "Desconocido"}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}