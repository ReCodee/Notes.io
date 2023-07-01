package com.example.notesio.backend.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesio.backend.model.Note
import com.example.notesio.backend.repository.MongoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
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
            if (data.value?.isNotEmpty() == true) {
                repository.insertPerson(note = Note().apply {
                    data = note.data
                    email = note.email
                })
            }
        }
    }

    /*fun updatePerson() {
        viewModelScope.launch(Dispatchers.IO) {
            if (objectId.value.isNotEmpty()) {
                repository.updatePerson(person = Person().apply {
                    _id = ObjectId(hexString = this@HomeViewModel.objectId.value)
                    name = this@HomeViewModel.name.value
                })
            }
        }
    }*/

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