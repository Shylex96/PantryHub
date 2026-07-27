package com.pantryhub.feature.shopping.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryEmptyState
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.components.PantryTopBar
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.feature.shopping.presentation.ShoppingUiState
import com.pantryhub.feature.shopping.ui.components.ShoppingListCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListsScreen(
    state: ShoppingUiState,
    onListClick: (String) -> Unit,
    onDeleteList: (String) -> Unit,
    onCreateList: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = PantryHubTheme.spacing
    var showCreateDialog by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }
    
    val focusRequester = remember { FocusRequester() }

    var listToDelete by remember { mutableStateOf<String?>(null) }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text(stringResource(R.string.create_list_dialog_title)) },
            text = {
                PantryTextField(
                    value = newListName,
                    onValueChange = { newListName = it },
                    label = stringResource(R.string.list_name_placeholder),
                    modifier = Modifier.focusRequester(focusRequester)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newListName.isNotBlank()) {
                        onCreateList(newListName)
                        newListName = ""
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
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
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
                onClick = { showCreateDialog = true },
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
                    .padding(horizontal = spacing.lg)
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
