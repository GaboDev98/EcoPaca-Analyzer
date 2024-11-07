package com.gabodev.ecopacaanalyzer

import com.gabodev.ecopacaanalyzer.data.PacaRepository
import com.gabodev.ecopacaanalyzer.models.Reading
import com.gabodev.ecopacaanalyzer.models.User
import com.gabodev.ecopacaanalyzer.utils.orEmpty
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebasePacaRepository : PacaRepository {
    private val database = FirebaseDatabase.getInstance()

    override suspend fun getUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            try {
                val ref: DatabaseReference = database.getReference("UsersData")
                val snapshot = ref.get().await()

                val users = mutableListOf<User>()
                snapshot.children.forEach { userSnapshot ->
                    val userId = userSnapshot.key ?: return@forEach
                    val readings = mutableMapOf<String, Reading>()

                    userSnapshot.child("readings").children.forEach { readingSnapshot ->
                        val timestamp = readingSnapshot.key ?: return@forEach
                        val humidity =
                            readingSnapshot.child("humidity").getValue(String::class.java) ?: "0"
                        val pressure =
                            readingSnapshot.child("pressure").getValue(String::class.java) ?: "0"
                        val temperature =
                            readingSnapshot.child("temperature").getValue(String::class.java) ?: "0"

                        val reading = Reading(
                            humidity = humidity,
                            pressure = pressure,
                            temperature = temperature,
                            timestamp = timestamp
                        )
                        readings[timestamp] = reading
                    }

                    val user = User(userId, readings)
                    users.add(user)
                }
                users
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getReadings(userId: String): Map<String, Reading> {
        return withContext(Dispatchers.IO) {
            try {
                val ref: DatabaseReference = database.getReference("UsersData/$userId/readings")
                val snapshot = ref.get().await()

                val readings = mutableMapOf<String, Reading>()
                snapshot.children.forEach { readingSnapshot ->
                    val timestamp = readingSnapshot.key ?: return@forEach
                    val humidity =
                        readingSnapshot.child("humidity").getValue(String::class.java) ?: "0"
                    val pressure =
                        readingSnapshot.child("pressure").getValue(String::class.java) ?: "0"
                    val temperature =
                        readingSnapshot.child("temperature").getValue(String::class.java) ?: "0"

                    val reading = Reading(
                        id = timestamp,
                        humidity = humidity,
                        pressure = pressure,
                        temperature = temperature,
                        timestamp = timestamp
                    )
                    readings[timestamp] = reading
                }
                readings
            } catch (e: Exception) {
                emptyMap()
            }
        }
    }

    override suspend fun getReadingDetail(userId: String, readingId: String): Reading? {

        val snapshot = database.getReference()
            .child("UsersData/$userId/readings/$readingId")
            .get()
            .await()

        return if (snapshot.exists()) {
            val humidity = snapshot.child("humidity").getValue(String::class.java).orEmpty()
            val pressure = snapshot.child("pressure").getValue(String::class.java).orEmpty()
            val temperature = snapshot.child("temperature").getValue(String::class.java).orEmpty()
            val timestamp = snapshot.child("timestamp").getValue(String::class.java).orEmpty()

            Reading(readingId, humidity, pressure, temperature, timestamp)
        } else {
            null
        }
    }
}