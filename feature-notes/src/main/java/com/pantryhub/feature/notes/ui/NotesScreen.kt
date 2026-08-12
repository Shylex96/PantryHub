package com.pantryhub.feature.notes.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryCard
import com.pantryhub.core.designsystem.ui.components.PantryEmptyState
import com.pantryhub.core.designsystem.ui.components.PantryListItem
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.components.PantryTopBar
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.model.note.Note
import com.pantryhub.feature.notes.presentation.NotesViewModel

@Composable
fun NotesScreen(modifier: Modifier = Modifier) {
    val viewModel: NotesViewModel = hiltViewModel()
    val notes by viewModel.notes.collectAsState()
    val spacing = PantryHubTheme.spacing

    var editorOpen by remember { mutableStateOf(false) }
    var editingId by remember { mutableStateOf<String?>(null) }
    var titleInput by remember { mutableStateOf("") }
    var contentInput by remember { mutableStateOf("") }

    fun openEditor(note: Note?) {
        editingId = note?.id
        titleInput = note?.title ?: ""
        contentInput = note?.content ?: ""
        editorOpen = true
    }

    if (editorOpen) {
        AlertDialog(
            onDismissRequest = { editorOpen = false },
            title = {
                Text(
                    stringResource(
                        if (editingId == null) R.string.note_new_title else R.string.note_edit_title
                    )
                )
            },
            text = {
                Column {
                    PantryTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = stringResource(R.string.note_title_label),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(spacing.md))
                    PantryTextField(
                        value = contentInput,
                        onValueChange = { contentInput = it },
                        label = stringResource(R.string.note_content_label),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (titleInput.isNotBlank() || contentInput.isNotBlank()) {
                        viewModel.save(editingId, titleInput, contentInput)
                        editorOpen = false
                    }
                }) {
                    Text(stringResource(R.string.save_action))
                }
            },
            dismissButton = {
                TextButton(onClick = { editorOpen = false }) {
                    Text(stringResource(R.string.cancel_action))
                }
            }
        )
    }

    Scaffold(
        topBar = { PantryTopBar(title = stringResource(R.string.nav_notes)) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openEditor(null) },
                shape = PantryHubTheme.shapes.medium
            ) {
                Icon(
                    imageVector = PantryIcons.Add,
                    contentDescription = stringResource(R.string.note_new_title)
                )
            }
        }
    ) { innerPadding ->
        if (notes.isEmpty()) {
            PantryEmptyState(
                title = stringResource(R.string.empty_notes_title),
                description = stringResource(R.string.empty_notes_desc),
                icon = PantryIcons.Notes
            )
        } else {
            LazyColumn(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(horizontal = spacing.lg)
            ) {
                items(notes, key = { it.id }) { note ->
                    val untitled = stringResource(R.string.note_untitled)
                    PantryCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = spacing.xs)
                    ) {
                        PantryListItem(
                            title = note.title.ifBlank { untitled },
                            subtitle = note.content.ifBlank { null },
                            onClick = { openEditor(note) },
                            trailingContent = {
                                IconButton(
                                    onClick = { viewModel.delete(note) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = PantryIcons.Delete,
                                        contentDescription = stringResource(R.string.delete_action),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
