package com.example.notesio.backend.repository
import android.util.Log
import com.example.notesio.backend.model.Note
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class MongoRepositoryImpl(private val realm : Realm) : MongoRepository {
    override fun getData(): Flow<List<Note>> {
        return realm.query<Note>().asFlow().map { it.list }
    }

    override fun filterData(data: String): Flow<List<Note>> {
        return realm.query<Note>(query = "name CONTAINS[c] $0", data).asFlow().map { it.list }
    }

    override suspend fun insertPerson(note: Note) {
        realm.write { copyToRealm(note) }
    }

    override suspend fun updatePerson(note: Note) {
        realm.write {
            val queriedPerson = query<Note>(query = "_id == $0", note._id).first().find()
            queriedPerson?.data = note.data
        }
    }

    override suspend fun deletePerson(id: ObjectId) {
        realm.write {
            val person = query<Note>(query = "_id == $0", id).first().find()
            try {
                person?.let { delete(it) }
            } catch (e: Exception) {
                Log.d("MongoRepositoryImpl", "${e.message}")
            }
        }
    }
}