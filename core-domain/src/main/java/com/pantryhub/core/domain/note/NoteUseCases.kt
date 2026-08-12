package com.pantryhub.core.domain.note

import javax.inject.Inject

class NoteUseCases @Inject constructor(
    val getNotes: GetNotesUseCase,
    val saveNote: SaveNoteUseCase,
    val deleteNote: DeleteNoteUseCase
)
