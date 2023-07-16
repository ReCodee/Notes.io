package com.example.notesio.backend.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesio.backend.model.Note
import com.example.notesio.backend.repository.MongoRepository
import com.example.notesio.backend.repository.MongoRepositoryImpl
import com.example.notesio.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import javax.inject.Inject


class NoteViewModel () : ViewModel() {
    val data = MutableLiveData<List<Note>>()
    //val data: LiveData<List<Note>> = mutableData


    init {
        viewModelScope.launch {
            MongoRepositoryImpl.filterData("abhishek27082000@gmail.com").collect {
                data.value = it
            }
        }
    }

    /*fun updateName(name: String) {
        this.name.value = name
    }*/

    /*fun updateObjectId(id: String) {
        this.objectId.value = id
    }*/


    @SuppressLint("SuspiciousIndentation")
    fun insertNote(note:Note) {
        viewModelScope.launch(Dispatchers.IO) {
                /*val notes : MutableList<Note>? = data.value?.toMutableList();
                notes?.add(note)
                data.value = notes?.toList()*/
            MongoRepositoryImpl.insertPerson(note = Note().apply {
                    _id = note._id
                    data = note.data
                    title = note.title
                    email = note.email
                })
            var refreshed : List<Note> = emptyList()
                MongoRepositoryImpl.getData().collect {
                refreshed = it
            }
            withContext(Dispatchers.Main) {
                data.value = refreshed
            }
        }
    }

    fun updateNote(note:Note) {
        viewModelScope.launch(Dispatchers.IO) {
                MongoRepositoryImpl.updatePerson(note = Note().apply {
                    _id = note._id
                    email = note.email
                    title = note.title
                    data = note.data
                })
        }
    }

    /*fun deletePerson() {
        viewModelScope.launch {
            if (objectId.value.isNotEmpty()) {
                repository.deletePerson(id = ObjectId(hexString = objectId.value))
            }
        }
    }*/

    /*fun filterData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (filtered.value) {
                repository.getData().collect {
                    filtered.value = false
                    name.value = ""
                    data.value = it
                }
            } else {
                repository.filterData(name = name.value).collect {
                    filtered.value = true
                    data.value = it
                }
            }
        }
    }*/
}