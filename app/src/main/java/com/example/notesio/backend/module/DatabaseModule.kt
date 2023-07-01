package com.example.notesio.backend.module

import com.example.notesio.backend.model.Note
import com.example.notesio.backend.repository.MongoRepository
import com.example.notesio.backend.repository.MongoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                Note::class
            )
        ).name("Notes.io")
            .compactOnLaunch()
            .build()
        return Realm.open(config)
    }
    @Singleton
    @Provides
    fun provideMongoRepository(realm: Realm): MongoRepository {
        return MongoRepositoryImpl(realm = realm)
    }
}