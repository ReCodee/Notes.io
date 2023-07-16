package com.example.notesio.backend.model

import android.location.Address
import android.os.Parcelable
import android.text.Spannable
import android.text.SpannableString
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.mongodb.kbson.ObjectId

@Parcelize
class Note : RealmObject, Parcelable {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var owner_id: String = ""
    var email: String = ""
    var title: String = ""
    var data: String = ""
    var timestamp: RealmInstant = RealmInstant.now()
}