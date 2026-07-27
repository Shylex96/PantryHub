package com.pantryhub.feature.shopping.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryEmptyState
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.components.PantryTopBar
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

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text(stringResource(R.string.create_list_dialog_title)) },
            text = {
                PantryTextField(
                    value = newListName,
                    onValueChange = { newListName = it },
                    label = stringResource(R.string.list_name_placeholder)
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
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.create_list_description)
                )
            }
        }
    ) { innerPadding ->
        if (state.lists.isEmpty() && !state.isLoading) {
            PantryEmptyState(
                title = stringResource(R.string.empty_shopping_lists_title),
                description = stringResource(R.string.empty_shopping_lists_description),
                icon = Icons.Default.Info
            )
        } else {
            LazyColumn(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(horizontal = spacing.lg)
            ) {
                items(state.lists, key = { it.id }) { list ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            if (it == SwipeToDismissBoxValue.EndToStart) {
                                onDeleteList(list.id)
                                true
                            } else false
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false,
                        backgroundContent = {
                            val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                                MaterialTheme.colorScheme.error
                            } else Color.Transparent
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color, PantryHubTheme.shapes.medium)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete_action),
                                    tint = Color.White
                                )
                            }
                        }
                    ) {
                        ShoppingListCard(
                            shoppingList = list,
                            onListClick = onListClick,
                            onDelete = onDeleteList
                        )
                    }
                }
            }
        }
    }
}
