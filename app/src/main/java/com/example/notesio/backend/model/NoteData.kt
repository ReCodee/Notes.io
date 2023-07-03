package com.example.notesio.backend.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

@Parcelize
data class NoteData(
    val title : String,
    val body  : String,
    val id : String,
) : Parcelable
