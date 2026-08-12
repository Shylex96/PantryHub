package com.pantryhub.core.domain.note

import com.pantryhub.core.data.repository.NoteRepository
import com.pantryhub.core.model.note.Note
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.saveNote(note)
    }
}
