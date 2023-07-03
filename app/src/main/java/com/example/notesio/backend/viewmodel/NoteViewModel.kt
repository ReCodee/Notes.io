package com.example.notesio.backend.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesio.backend.model.Note
import com.example.notesio.backend.repository.MongoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: MongoRepository
) : ViewModel() {
    val data = MutableLiveData<List<Note>>()
    //val data: LiveData<List<Note>> = mutableData


    init {
        viewModelScope.launch {
            repository.getData().collect {
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


    fun insertNote(note:Note) {
        viewModelScope.launch(Dispatchers.IO) {
                /*val notes : MutableList<Note>? = data.value?.toMutableList();
                notes?.add(note)
                data.value = notes?.toList()*/
                repository.insertPerson(note = Note().apply {
                    _id = note._id
                    data = note.data
                    email = note.email
                })
            var refreshed : List<Note> = emptyList()
                repository.getData().collect {
                refreshed = it
            }
            withContext(Dispatchers.Main) {
                data.value = refreshed
            }
        }
    }

    fun updateNote(note:Note) {
        viewModelScope.launch(Dispatchers.IO) {
                repository.updatePerson(note = Note().apply {
                    _id = note._id
                    email = note.email
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