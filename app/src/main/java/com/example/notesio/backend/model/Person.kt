package com.example.notesio.backend.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Person : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var name: String = ""
    var notes: RealmList<Note> = realmListOf()
    var timestamp: RealmInstant = RealmInstant.now()
}