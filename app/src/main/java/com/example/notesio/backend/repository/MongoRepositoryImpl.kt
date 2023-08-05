package com.example.notesio.backend.repository
import android.content.SharedPreferences
import android.util.Log
import com.example.notesio.backend.model.Note
import com.example.notesio.utils.Constants.APP_ID
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

object MongoRepositoryImpl {
    /*override fun getData(): Flow<List<Note>> {
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
    }*/

/*    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm
    var sharedPreferences : SharedPreferences? = null

    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(
                user,
                setOf(Note::class)
            )
                .initialSubscriptions { sub ->
                    add(query = sub.query<Note>(query = "owner_id == $0", user.id))
                }
                .log(LogLevel.ALL)
                .build()
            realm = Realm.open(config)
        } else {
            Log.d("Realm Check","Didn't Initialize")
        }
    }

    override fun getData(): Flow<List<Note>> {
        return realm.query<Note>().asFlow().map { it.list }
    }

    override fun filterData(data: String): Flow<List<Note>> {
        return realm.query<Note>(query = "email CONTAINS[c] $0", data)
            .asFlow().map { it.list }
    }

    override suspend fun insertPerson(note : Note) {
        if (user != null) {
            realm.write {
                try {
                    copyToRealm(note.apply { owner_id = user.id })
                } catch (e: Exception) {
                    Log.d("MongoRepository", e.message.toString())
                }
            }
        }
    }

    override suspend fun updatePerson(note:Note) {
        realm.write {
            val queriedPerson =
                query<Note>(query = "_id == $0", note._id)
                    .first()
                    .find()
            if (queriedPerson != null) {
                queriedPerson.data = note.data
                queriedPerson.title = note.title
            } else {
                Log.d("MongoRepository", "Queried Person does not exist.")
            }
        }
    }

    override suspend fun deletePerson(id: ObjectId) {
        realm.write {
            try {
                val person = query<Note>(query = "_id == $0", id)
                    .first()
                    .find()
                person?.let { delete(it) }
            } catch (e: Exception) {
                Log.d("MongoRepository", "${e.message}")
            }
        }
    }*/

}