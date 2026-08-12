package com.pantryhub.core.data.repository

import com.pantryhub.core.model.note.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<List<Note>>
    suspend fun getNote(id: String): Note?
    suspend fun saveNote(note: Note)
    suspend fun deleteNote(note: Note)
}
