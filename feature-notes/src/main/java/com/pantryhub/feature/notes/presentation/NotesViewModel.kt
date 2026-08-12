package com.pantryhub.feature.notes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantryhub.core.domain.note.NoteUseCases
import com.pantryhub.core.model.note.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    val notes: StateFlow<List<Note>> = noteUseCases.getNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Creates a new note when [id] is null, otherwise updates the existing one. */
    fun save(id: String?, title: String, content: String) {
        viewModelScope.launch {
            val now = Clock.System.now()
            val existing = id?.let { existingId -> notes.value.find { it.id == existingId } }
            noteUseCases.saveNote(
                Note(
                    id = id ?: UUID.randomUUID().toString(),
                    title = title.trim(),
                    content = content.trim(),
                    createdAt = existing?.createdAt ?: now,
                    updatedAt = now
                )
            )
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch { noteUseCases.deleteNote(note) }
    }
}
