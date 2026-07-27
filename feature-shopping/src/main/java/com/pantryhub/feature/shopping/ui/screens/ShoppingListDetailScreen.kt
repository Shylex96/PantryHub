package com.pantryhub.feature.shopping.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryButton
import com.pantryhub.core.designsystem.ui.components.PantryListItem
import com.pantryhub.core.designsystem.ui.components.PantryLoading
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.components.PantryTopBar
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.feature.shopping.presentation.ShoppingUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListDetailScreen(
    state: ShoppingUiState,
    onAddItem: (String, Double) -> Unit,
    onDeleteItem: (String) -> Unit,
    onDeleteList: (String) -> Unit,
    onRenameList: (String) -> Unit,
    onStartShopping: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentList = state.currentList ?: return
    var newItemName by remember { mutableStateOf("") }
    val spacing = PantryHubTheme.spacing
    
    var showRenameDialog by remember { mutableStateOf(false) }
    var listNameBuffer by remember { mutableStateOf(currentList.name) }
    val renameFocusRequester = remember { FocusRequester() }

    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = { Text(stringResource(R.string.rename_list_dialog_title)) },
            text = {
                PantryTextField(
                    value = listNameBuffer,
                    onValueChange = { listNameBuffer = it },
                    label = stringResource(R.string.list_name_placeholder),
                    modifier = Modifier.focusRequester(renameFocusRequester)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onRenameList(listNameBuffer)
                    showRenameDialog = false
                }) {
                    Text(stringResource(R.string.save_action))
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = false }) {
                    Text(stringResource(R.string.cancel_action))
                }
            }
        )
        LaunchedEffect(Unit) {
            renameFocusRequester.requestFocus()
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text(stringResource(R.string.delete_list_confirm_title)) },
            text = { Text(stringResource(R.string.delete_list_confirm_message, currentList.name)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteList(currentList.id)
                        showDeleteConfirm = false
                        onBack()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.delete_confirm_action)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text(stringResource(R.string.cancel_action))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            PantryTopBar(
                title = currentList.name,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = PantryIcons.Back, 
                            contentDescription = stringResource(R.string.back_description)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        listNameBuffer = currentList.name
                        showRenameDialog = true 
                    }) {
                        Icon(PantryIcons.Edit, contentDescription = stringResource(R.string.rename_list_dialog_title))
                    }
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(PantryIcons.Delete, contentDescription = stringResource(R.string.delete_action))
                    }
                }
            )
        },
        bottomBar = {
            if (currentList.items.isNotEmpty()) {
                PantryButton(
                    onClick = onStartShopping,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.lg)
                ) {
                    Text(stringResource(R.string.start_shopping_action))
                }
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            PantryLoading()
        } else {
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(horizontal = spacing.lg)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PantryTextField(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        label = stringResource(R.string.add_item_label),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            if (newItemName.isNotBlank()) {
                                onAddItem(newItemName, 1.0)
                                newItemName = ""
                            }
                        }
                    ) {
                        Icon(
                            imageVector = PantryIcons.Add, 
                            contentDescription = stringResource(R.string.add_item_label)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(spacing.md))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(currentList.items, key = { it.id }) { item ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = {
                                if (it == SwipeToDismissBoxValue.EndToStart) {
                                    onDeleteItem(item.id)
                                    true
                                } else false
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                val progress = dismissState.progress
                                val intenseRed = Color(0xFFB00020)
                                val colorAlpha = (progress * 2f).coerceAtMost(1f)
                                
                                val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                                    intenseRed.copy(alpha = colorAlpha)
                                } else Color.Transparent
                                
                                // Background icon: Reveal after swipe starts to avoid ghosting
                                val iconProgress = ((progress - 0.20f) / 0.80f).coerceAtLeast(0f)
                                val scale = 0.5f + (iconProgress * 0.5f).coerceAtMost(0.7f)
                                val iconAlpha = (iconProgress * 3f).coerceAtMost(1f)

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color, PantryHubTheme.shapes.medium)
                                        .padding(horizontal = spacing.xl),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        imageVector = PantryIcons.Delete,
                                        contentDescription = stringResource(R.string.delete_action),
                                        // tint = Color.White.copy(alpha = iconAlpha),
                                        modifier = Modifier.scale(scale)
                                    )
                                }
                            }
                        ) {
                            PantryListItem(
                                title = item.product.name,
                                subtitle = null,
                                trailingContent = {
                                    IconButton(
                                        onClick = { onDeleteItem(item.id) },
                                        modifier = Modifier
                                            .size(28.dp)
                                            .alpha(if (dismissState.progress > 0.05f) 0f else 0.6f)
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
}
