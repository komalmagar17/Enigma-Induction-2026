package com.example.quicknotes

import android.app.Application
import androidx.room.Room
import com.example.quicknotes.data.local.NoteDatabase
import com.example.quicknotes.data.repository.NoteRepositoryImpl
import com.example.quicknotes.domain.repository.NoteRepository

class NoteApp : Application() {
    val database by lazy {
        Room.databaseBuilder(
            this,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    val repository by lazy {
        NoteRepositoryImpl(database.noteDao)
    }
}
