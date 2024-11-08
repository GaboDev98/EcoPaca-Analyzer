package com.gabodev.ecopacaanalyzer.data

import com.gabodev.ecopacaanalyzer.models.Reading
import com.gabodev.ecopacaanalyzer.models.User
import com.gabodev.ecopacaanalyzer.utils.orEmpty
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebasePacaRepository : PacaRepository {
    private val database = FirebaseDatabase.getInstance()

    override suspend fun getReadingDetail(userId: String, readingId: String): Reading? {
        return withContext(Dispatchers.IO) {
            try {
                val snapshot = database.getReference("UsersData/$userId/readings/$readingId").get().await()
                if (snapshot.exists()) parseReadingSnapshot(snapshot) else null
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun listenForUsersUpdates(usersFlow: MutableStateFlow<List<User>>) {
        database.getReference("UsersData").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val usersList = snapshot.children.mapNotNull { parseUserSnapshot(it) }
                    usersFlow.update { usersList }
                } catch (e: Exception) { }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun listenForReadingsUpdates(userId: String, readingsFlow: MutableStateFlow<Map<String, Reading>>) {
        val ref: DatabaseReference = database.getReference("UsersData/$userId/readings")

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

    private fun parseUserSnapshot(userSnapshot: DataSnapshot): User? {
        val userId = userSnapshot.key ?: return null
        val readings = userSnapshot.child("readings").children.mapNotNull { parseReadingSnapshot(it) }
            .associateBy { it.timestamp }
        return User(id = userId, readings = readings)
    }

    private fun parseReadingSnapshot(readingSnapshot: DataSnapshot): Reading? {
        val timestamp = readingSnapshot.key.orEmpty()
        val humidity = readingSnapshot.child("humidity").getValue(String::class.java).orEmpty()
        val pressure = readingSnapshot.child("pressure").getValue(String::class.java).orEmpty()
        val temperature = readingSnapshot.child("temperature").getValue(String::class.java).orEmpty()
        return if (timestamp.isNotEmpty()) {
            Reading(
                humidity = humidity,
                pressure = pressure,
                temperature = temperature,
                timestamp = timestamp
            )
        } else null
    }
}