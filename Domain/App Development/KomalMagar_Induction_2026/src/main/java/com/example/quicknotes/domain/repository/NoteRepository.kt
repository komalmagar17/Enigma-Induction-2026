package com.example.quicknotes.domain.repository

import com.example.quicknotes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: String): Note?
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun searchNotes(query: String): Flow<List<Note>>
}
