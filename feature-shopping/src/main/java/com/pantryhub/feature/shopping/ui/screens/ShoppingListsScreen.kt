package com.pantryhub.feature.shopping.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryEmptyState
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.components.PantryTopBar
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.model.shopping.ShoppingListType
import com.pantryhub.feature.shopping.presentation.ShoppingUiState
import com.pantryhub.feature.shopping.ui.components.ShoppingListCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListsScreen(
    state: ShoppingUiState,
    onListClick: (String) -> Unit,
    onDeleteList: (String) -> Unit,
    onCreateList: (String, ShoppingListType) -> Unit,
    onCloneList: (String, String, ShoppingListType) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = PantryHubTheme.spacing
    var showCreateDialog by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }
    var newListType by remember { mutableStateOf(ShoppingListType.REGULAR) }
    var cloneSourceId by remember { mutableStateOf<String?>(null) }
    var cloneMenuExpanded by remember { mutableStateOf(false) }

    var listToDelete by remember { mutableStateOf<String?>(null) }

    if (showCreateDialog) {
        val cloneSourceName = state.lists.find { it.id == cloneSourceId }?.name
            ?: stringResource(R.string.clone_none)
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text(stringResource(R.string.create_list_dialog_title)) },
            text = {
                Column {
                    PantryTextField(
                        value = newListName,
                        onValueChange = { newListName = it },
                        label = stringResource(R.string.list_name_placeholder),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(spacing.lg))
                    Text(
                        text = stringResource(R.string.list_type_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(spacing.xs))
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = newListType == ShoppingListType.REGULAR,
                            onClick = { newListType = ShoppingListType.REGULAR },
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                        ) {
                            Text(stringResource(R.string.list_type_regular))
                        }
                        SegmentedButton(
                            selected = newListType == ShoppingListType.TEMPORARY,
                            onClick = { newListType = ShoppingListType.TEMPORARY },
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                        ) {
                            Text(stringResource(R.string.list_type_temporary))
                        }
                    }

                    Spacer(modifier = Modifier.height(spacing.lg))
                    Text(
                        text = stringResource(R.string.clone_from_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(spacing.xs))
                    Box {
                        AssistChip(
                            onClick = { cloneMenuExpanded = true },
                            label = { Text(cloneSourceName) }
                        )
                        DropdownMenu(
                            expanded = cloneMenuExpanded,
                            onDismissRequest = { cloneMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.clone_none)) },
                                onClick = {
                                    cloneSourceId = null
                                    cloneMenuExpanded = false
                                }
                            )
                            state.lists.forEach { list ->
                                DropdownMenuItem(
                                    text = { Text(list.name) },
                                    onClick = {
                                        cloneSourceId = list.id
                                        cloneMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newListName.isNotBlank()) {
                        val source = cloneSourceId
                        if (source != null) {
                            onCloneList(source, newListName, newListType)
                        } else {
                            onCreateList(newListName, newListType)
                        }
                        showCreateDialog = false
                    }
                }) {
                    Text(stringResource(R.string.save_action))
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text(stringResource(R.string.cancel_action))
                }
            }
        )
    }

    if (listToDelete != null) {
        val listName = state.lists.find { it.id == listToDelete }?.name ?: ""
        AlertDialog(
            onDismissRequest = { listToDelete = null },
            title = { Text(stringResource(R.string.delete_list_confirm_title)) },
            text = { Text(stringResource(R.string.delete_list_confirm_message, listName)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        listToDelete?.let(onDeleteList)
                        listToDelete = null
                    }
                ) {
                    Text(
                        text = stringResource(R.string.delete_confirm_action)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { listToDelete = null }) {
                    Text(stringResource(R.string.cancel_action))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            PantryTopBar(title = stringResource(R.string.shopping_lists_title))
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    newListName = ""
                    newListType = ShoppingListType.REGULAR
                    cloneSourceId = null
                    showCreateDialog = true
                },
                shape = PantryHubTheme.shapes.medium
            ) {
                Icon(
                    imageVector = PantryIcons.Add,
                    contentDescription = stringResource(R.string.create_list_description)
                )
            }
        }
    ) { innerPadding ->
        if (state.lists.isEmpty() && !state.isLoading) {
            PantryEmptyState(
                title = stringResource(R.string.empty_shopping_lists_title),
                description = stringResource(R.string.empty_shopping_lists_description),
                icon = PantryIcons.Lists
            )
        } else {
            LazyColumn(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(horizontal = spacing.lg),
                contentPadding = PaddingValues(top = spacing.sm, bottom = spacing.xxl)
            ) {
                items(state.lists, key = { it.id }) { list ->
                    ShoppingListCard(
                        shoppingList = list,
                        onListClick = onListClick,
                        onDelete = { listToDelete = it }
                    )
                }
            }
        }
    }
}
