package com.gabodev.ecopacaanalyzer

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebasePacaRepository : PacaRepository {
    private val database = FirebaseDatabase.getInstance()

    override suspend fun getPacaReadings(): List<PacaReading> {
        val snapshot =
            database.getReference().child("UsersData/nDGkRbD2cfcdrIadyCmOeQaGi4I2/readings").get()
                .await()
        return snapshot.children.mapNotNull { dataSnapshot ->
            val id = dataSnapshot.key ?: return@mapNotNull null
            val humidity = dataSnapshot.child("humidity").getValue(String::class.java) ?: ""
            val pressure = dataSnapshot.child("pressure").getValue(String::class.java) ?: ""
            val temperature = dataSnapshot.child("temperature").getValue(String::class.java) ?: ""
            val timestamp = dataSnapshot.child("timestamp").getValue(String::class.java) ?: ""
            PacaReading(id, humidity, pressure, temperature, timestamp)
        }
    }

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

                        val reading = Reading(humidity, pressure, temperature, timestamp)
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

                    val reading = Reading(humidity, pressure, temperature, timestamp)
                    readings[timestamp] = reading
                }
                readings
            } catch (e: Exception) {
                emptyMap()
            }
        }
    }
}