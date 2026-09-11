package com.pantryhub.feature.notes.ui

import android.text.format.DateUtils
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryButton
import com.pantryhub.core.designsystem.ui.components.PantryDialog
import com.pantryhub.core.designsystem.ui.components.PantryEmptyState
import com.pantryhub.core.designsystem.ui.components.PantryExtendedFab
import com.pantryhub.core.designsystem.ui.components.PantryFieldLabel
import com.pantryhub.core.designsystem.ui.components.PantryHeaderIconButton
import com.pantryhub.core.designsystem.ui.components.PantryScreenHeader
import com.pantryhub.core.designsystem.ui.components.PantrySearchField
import com.pantryhub.core.designsystem.ui.components.PantrySheet
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.model.note.Note
import com.pantryhub.feature.notes.presentation.NotesViewModel

/**
 * Notes tab (docs/05_Design_System.md §6.1): large header with the note count and a
 * search toggle, a two-column grid of note cards (title, excerpt, relative date) and an
 * extended FAB. Tapping a card opens the editor sheet; deletion lives inside the editor.
 */
@Composable
fun NotesScreen(modifier: Modifier = Modifier) {
    val viewModel: NotesViewModel = hiltViewModel()
    val notes by viewModel.notes.collectAsState()
    val spacing = PantryHubTheme.spacing

    var searchOpen by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var editorOpen by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<Note?>(null) }

    val visibleNotes = remember(notes, query) {
        val q = query.trim()
        if (q.isEmpty()) {
            notes
        } else {
            notes.filter { it.title.contains(q, ignoreCase = true) || it.content.contains(q, ignoreCase = true) }
        }
    }

    if (editorOpen) {
        NoteEditorSheet(
            note = editingNote,
            onSave = { title, content ->
                viewModel.save(editingNote?.id, title, content)
                editorOpen = false
            },
            onDelete = { note ->
                viewModel.delete(note)
                editorOpen = false
            },
            onDismiss = { editorOpen = false }
        )
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            PantryExtendedFab(
                text = stringResource(R.string.fab_new_note),
                icon = PantryIcons.Add,
                onClick = {
                    editingNote = null
                    editorOpen = true
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            PantryScreenHeader(
                title = stringResource(R.string.nav_notes),
                subtitle = if (notes.isEmpty()) {
                    null
                } else {
                    pluralStringResource(R.plurals.notes_count, notes.size, notes.size)
                },
                action = {
                    if (notes.isNotEmpty()) {
                        PantryHeaderIconButton(
                            icon = if (searchOpen) PantryIcons.Close else PantryIcons.Search,
                            contentDescription = stringResource(R.string.search_notes_placeholder),
                            onClick = {
                                searchOpen = !searchOpen
                                if (!searchOpen) query = ""
                            }
                        )
                    }
                },
                modifier = Modifier.padding(top = spacing.lg)
            )

            AnimatedVisibility(visible = searchOpen) {
                PantrySearchField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = stringResource(R.string.search_notes_placeholder),
                    clearContentDescription = stringResource(R.string.clear_search_action),
                    modifier = Modifier.padding(
                        start = spacing.screen,
                        end = spacing.screen,
                        top = spacing.lg
                    )
                )
            }

            when {
                notes.isEmpty() -> PantryEmptyState(
                    title = stringResource(R.string.empty_notes_title),
                    description = stringResource(R.string.empty_notes_desc),
                    icon = PantryIcons.Notes,
                    modifier = Modifier.padding(top = spacing.xl)
                )

                visibleNotes.isEmpty() -> PantryEmptyState(
                    title = stringResource(R.string.search_no_results_title),
                    description = stringResource(R.string.search_no_results_desc, query),
                    icon = PantryIcons.Search,
                    modifier = Modifier.padding(top = spacing.xl)
                )

                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = spacing.screen,
                        end = spacing.screen,
                        top = spacing.xl,
                        bottom = 96.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(visibleNotes, key = { it.id }, span = { GridItemSpan(1) }) { note ->
                        NoteCard(
                            note = note,
                            onClick = {
                                editingNote = note
                                editorOpen = true
                            },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = PantryHubTheme.spacing
    val relativeDate = remember(note.updatedAt) {
        DateUtils.getRelativeTimeSpanString(
            note.updatedAt.toEpochMilliseconds(),
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()
    }

    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(PantryHubTheme.radius.card),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(
            modifier = Modifier
                .defaultMinSize(minHeight = 150.dp)
                .padding(spacing.lg),
            verticalArrangement = Arrangement.spacedBy(spacing.sm)
        ) {
            Text(
                text = note.title.ifBlank { stringResource(R.string.note_untitled) },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (note.title.isBlank()) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (note.content.isNotBlank()) {
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = true)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f, fill = true))
            }
            Text(
                text = relativeDate,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

/** Create or edit a note. Existing notes can be deleted from here (with confirmation). */
@Composable
private fun NoteEditorSheet(
    note: Note?,
    onSave: (title: String, content: String) -> Unit,
    onDelete: (Note) -> Unit,
    onDismiss: () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var confirmDelete by remember { mutableStateOf(false) }

    if (confirmDelete && note != null) {
        PantryDialog(
            onDismissRequest = { confirmDelete = false },
            title = stringResource(R.string.delete_note_confirm_title),
            confirmButton = {
                TextButton(onClick = {
                    confirmDelete = false
                    onDelete(note)
                }) {
                    Text(stringResource(R.string.delete_confirm_action))
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) {
                    Text(stringResource(R.string.cancel_action))
                }
            }
        ) {
            Text(stringResource(R.string.delete_note_confirm_message))
        }
    }

    PantrySheet(
        onDismissRequest = onDismiss,
        title = stringResource(if (note == null) R.string.note_new_title else R.string.note_edit_title)
    ) {
        PantryFieldLabel(stringResource(R.string.note_title_label))
        Spacer(modifier = Modifier.height(spacing.sm))
        PantryTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = stringResource(R.string.note_title_placeholder),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(spacing.lg))
        PantryFieldLabel(stringResource(R.string.note_content_label))
        Spacer(modifier = Modifier.height(spacing.sm))
        PantryTextField(
            value = content,
            onValueChange = { content = it },
            placeholder = stringResource(R.string.note_content_placeholder),
            minLines = 6,
            maxLines = 12,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(spacing.xl))
        PantryButton(
            onClick = { onSave(title, content) },
            enabled = title.isNotBlank() || content.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
        if (note != null) {
            Spacer(modifier = Modifier.height(spacing.sm))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = { confirmDelete = true }) {
                    Text(
                        text = stringResource(R.string.delete_note_action),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
