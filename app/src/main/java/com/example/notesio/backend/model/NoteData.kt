package com.example.notesio.backend.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteData(
    val title : String,
    val body  : String
) : Parcelable
