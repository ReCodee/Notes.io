package com.example.notesio.backend.repository

import com.example.notesio.backend.model.Note
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface MongoRepository {
    fun configureTheRealm()
    fun getData(): Flow<List<Note>>
    fun filterData(data: String): Flow<List<Note>>
    suspend fun insertPerson(note: Note)
    suspend fun updatePerson(note: Note)
    suspend fun deletePerson(id: ObjectId)
}