package com.gabodev.ecopacaanalyzer.data

import com.gabodev.ecopacaanalyzer.models.Reading
import com.gabodev.ecopacaanalyzer.models.Device
import com.gabodev.ecopacaanalyzer.utils.orEmpty
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebasePacaRepository : PacaRepository {
    private val database = FirebaseDatabase.getInstance()

    override suspend fun getReadingDetail(deviceId: String, readingId: String): Reading? {
        return withContext(Dispatchers.IO) {
            try {
                val snapshot = database.getReference("DevicesData/$deviceId/readings/$readingId").get().await()
                if (snapshot.exists()) parseReadingSnapshot(snapshot) else null
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun listenForDevicesUpdates(devicesFlow: MutableStateFlow<List<Device>>) {
        database.getReference("DevicesData").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val devicesList = snapshot.children.mapNotNull { parseDeviceSnapshot(it) }
                    devicesFlow.update { devicesList }
                } catch (e: Exception) { }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun listenForReadingsUpdates(deviceId: String, readingsFlow: MutableStateFlow<Map<String, Reading>>) {
        val ref: DatabaseReference = database.getReference("DevicesData/$deviceId/readings")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val readingsMap = snapshot.children.mapNotNull { parseReadingSnapshot(it) }
                        .associateBy { it.timestamp }
                    readingsFlow.update { readingsMap }
                } catch (e: Exception) {}
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun parseDeviceSnapshot(deviceSnapshot: DataSnapshot): Device? {
        val deviceId = deviceSnapshot.key ?: return null
        val readings = deviceSnapshot.child("readings").children.mapNotNull { parseReadingSnapshot(it) }
            .associateBy { it.timestamp }
        return Device(id = deviceId, readings = readings)
    }

    private fun parseReadingSnapshot(readingSnapshot: DataSnapshot): Reading? {
        val timestamp = readingSnapshot.key.orEmpty()
        val humidity = readingSnapshot.child("humidity").getValue(String::class.java).orEmpty()
        val pressure = readingSnapshot.child("pressure").getValue(String::class.java).orEmpty()
        val temperature = readingSnapshot.child("temperature").getValue(String::class.java).orEmpty()
        return if (timestamp.isNotEmpty()) {
            Reading(
                id = timestamp,
                humidity = humidity,
                pressure = pressure,
                temperature = temperature,
                timestamp = timestamp
            )
        } else null
    }
}