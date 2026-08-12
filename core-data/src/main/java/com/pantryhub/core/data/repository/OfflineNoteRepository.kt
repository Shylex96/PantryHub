package com.pantryhub.core.data.repository

import com.pantryhub.core.database.dao.NoteDao
import com.pantryhub.core.database.mapper.asDomainModel
import com.pantryhub.core.database.mapper.asEntity
import com.pantryhub.core.model.note.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineNoteRepository @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map { it.asDomainModel() }
        }
    }

    override suspend fun getNote(id: String): Note? {
        return noteDao.getNoteById(id)?.asDomainModel()
    }

    override suspend fun saveNote(note: Note) {
        noteDao.insertNote(note.asEntity())
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNoteById(note.id)
    }
}
