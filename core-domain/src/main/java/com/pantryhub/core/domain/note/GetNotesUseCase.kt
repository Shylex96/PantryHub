package com.pantryhub.core.domain.note

import com.pantryhub.core.data.repository.NoteRepository
import com.pantryhub.core.model.note.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> = repository.getNotes()
}
