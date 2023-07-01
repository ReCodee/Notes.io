package com.example.notesio.backend.model

import android.location.Address
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Note : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var email: String = ""
    var data: String = ""
    var timestamp: RealmInstant = RealmInstant.now()
}