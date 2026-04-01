package com.example.quicknotes.data.repository

import com.example.quicknotes.data.local.NoteDao
import com.example.quicknotes.domain.model.Note
import com.example.quicknotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> = dao.getAllNotes()

    override suspend fun getNoteById(id: String): Note? = dao.getNoteById(id)

    override suspend fun insertNote(note: Note) = dao.insertNote(note)

    override suspend fun deleteNote(note: Note) = dao.deleteNote(note)

    override fun searchNotes(query: String): Flow<List<Note>> = dao.searchNotes(query)
}
