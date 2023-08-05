package com.example.notesio.backend.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesio.MainActivity
import com.example.notesio.backend.model.Note
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class NoteViewModel () : ViewModel() {
    val data = MutableLiveData<List<Note>>()
    //val data: LiveData<List<Note>> = mutableData



    init {

        viewModelScope.launch {
            delay(1000)
            getNoteData(onSuccess = {})
        }
    }

    fun getNoteData(onSuccess: () -> Unit) {
        viewModelScope.launch {
            MainActivity.dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val noteList : MutableList<Note> = emptyList<Note>().toMutableList()
                    val email = MainActivity.auth.currentUser?.email.toString()
                    if (snapshot.exists())  {
                        for (noteSnap in snapshot.children)  {
                            val noteData = noteSnap.getValue(Note::class.java)
                            if (noteData?.email == email)
                                noteList.add(noteData)
                        }
                    }
                    data.value = noteList
                    onSuccess()
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    fun getOneTimeNoteData(onSuccess: () -> Unit) {
        viewModelScope.launch {
            MainActivity.dbRef.get().addOnSuccessListener {
                    val noteList : MutableList<Note> = emptyList<Note>().toMutableList()
                    val email = MainActivity.auth.currentUser?.email.toString()
                    if (it.exists())  {
                        for (noteSnap in it.children)  {
                            val noteData = noteSnap.getValue(Note::class.java)
                            if (noteData?.email == email)
                                noteList.add(noteData)

                    }
                    data.value = noteList
                    onSuccess()
                }
            }
        }
    }

    fun getOneTimeNoteDataSessionCheck(onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            MainActivity.dbRef.get().addOnSuccessListener {
                val noteList : MutableList<Note> = emptyList<Note>().toMutableList()
                val email = MainActivity.auth.currentUser?.email.toString()
                if (it.exists())  {
                    for (noteSnap in it.children)  {
                        val noteData = noteSnap.getValue(Note::class.java)
                        if (noteData?.email == email)
                            noteList.add(noteData)

                    }
                    data.value = noteList
                    onSuccess()
                }
            }.addOnFailureListener {
                onError(it)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun insertNote(note:Note, onSuccess: () -> Unit,
                   onError: (Exception) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            MainActivity.dbRef.child(note.id).setValue(note)
                .addOnCompleteListener {
                    onSuccess()
                }.addOnFailureListener { err ->
                    onError(err)
                }
        }
    }
    fun updateNote(note:Note) {
        viewModelScope.launch(Dispatchers.IO) {
       MainActivity.dbRef.child(note.id).setValue(note)
        }
    }

    fun deleteNote(note:Note) {
        viewModelScope.launch {
        MainActivity.dbRef.child(note.id).removeValue()
        }
    }

    fun filterNotes(email:String) {
        viewModelScope.launch {

        }
    }

}