package com.pantryhub.feature.shopping.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryButton
import com.pantryhub.core.designsystem.ui.components.PantryChoiceCard
import com.pantryhub.core.designsystem.ui.components.PantryDialog
import com.pantryhub.core.designsystem.ui.components.PantryEmptyState
import com.pantryhub.core.designsystem.ui.components.PantryExtendedFab
import com.pantryhub.core.designsystem.ui.components.PantryFieldLabel
import com.pantryhub.core.designsystem.ui.components.PantryScreenHeader
import com.pantryhub.core.designsystem.ui.components.PantrySectionLabel
import com.pantryhub.core.designsystem.ui.components.PantrySheet
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.model.shopping.ShoppingList
import com.pantryhub.core.model.shopping.ShoppingListType
import com.pantryhub.feature.shopping.presentation.ShoppingUiState
import com.pantryhub.feature.shopping.ui.components.ShoppingListCard
import com.pantryhub.feature.shopping.ui.components.isShoppingInProgress

/**
 * Lists tab (docs/05_Design_System.md §6.1–6.2): large header with live counts, lists
 * grouped into "To buy" (has pending items) and "Up to date", an extended FAB that opens
 * the create sheet, and swipe-left to delete (with confirmation).
 */
@OptIn(ExperimentalMaterial3Api::class)
// SwipeToDismiss confirmValueChange is deprecated without a drop-in replacement.
@Suppress("DEPRECATION")
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
    val deleteColor = MaterialTheme.colorScheme.error

    var showCreateSheet by remember { mutableStateOf(false) }
    var listToDelete by remember { mutableStateOf<String?>(null) }

    val lists = state.lists
    val totalProducts = lists.sumOf { it.items.size }
    // "In progress" = shopping mode started (something already in the cart). Finishing a
    // shop resets the list, so it naturally drops back into "All lists".
    val inProgressLists = lists.filter { it.isShoppingInProgress }
    val otherLists = lists.filter { !it.isShoppingInProgress }

    if (showCreateSheet) {
        CreateListSheet(
            lists = lists,
            onDismiss = { showCreateSheet = false },
            onCreate = onCreateList,
            onClone = onCloneList
        )
    }

    val deletingId = listToDelete
    if (deletingId != null) {
        val listName = lists.find { it.id == deletingId }?.name ?: ""
        PantryDialog(
            onDismissRequest = { listToDelete = null },
            title = stringResource(R.string.delete_list_confirm_title),
            confirmButton = {
                TextButton(onClick = {
                    onDeleteList(deletingId)
                    listToDelete = null
                }) {
                    Text(stringResource(R.string.delete_confirm_action))
                }
            },
            dismissButton = {
                TextButton(onClick = { listToDelete = null }) {
                    Text(stringResource(R.string.cancel_action))
                }
            }
        ) {
            Text(stringResource(R.string.delete_list_confirm_message, listName))
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        // The app-level Scaffold already applies the system insets.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            PantryExtendedFab(
                text = stringResource(R.string.fab_new_list),
                icon = PantryIcons.Add,
                onClick = { showCreateSheet = true }
            )
        }
    ) { innerPadding ->
        val header: @Composable () -> Unit = {
            PantryScreenHeader(
                title = stringResource(R.string.nav_lists),
                subtitle = if (lists.isEmpty()) {
                    null
                } else {
                    pluralStringResource(R.plurals.lists_count, lists.size, lists.size) +
                        " · " +
                        pluralStringResource(R.plurals.products_count, totalProducts, totalProducts)
                },
                modifier = Modifier.padding(top = spacing.lg)
            )
        }

        if (lists.isEmpty() && !state.isLoading) {
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                header()
                PantryEmptyState(
                    title = stringResource(R.string.empty_shopping_lists_title),
                    description = stringResource(R.string.empty_shopping_lists_description),
                    icon = PantryIcons.Lists
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                item { header() }

                if (inProgressLists.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(28.dp))
                        PantrySectionLabel(
                            text = stringResource(R.string.lists_section_in_progress),
                            color = MaterialTheme.colorScheme.primary,
                            count = inProgressLists.size,
                            modifier = Modifier.padding(horizontal = spacing.screen)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    items(inProgressLists, key = { it.id }) { list ->
                        SwipeableListCard(
                            list = list,
                            deleteColor = deleteColor,
                            onListClick = onListClick,
                            onRequestDelete = { listToDelete = it }
                        )
                    }
                }

                if (otherLists.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(if (inProgressLists.isEmpty()) 28.dp else 16.dp))
                        // When nothing is in progress the label is redundant noise: skip it.
                        if (inProgressLists.isNotEmpty()) {
                            PantrySectionLabel(
                                text = stringResource(R.string.lists_section_all),
                                count = otherLists.size,
                                modifier = Modifier.padding(horizontal = spacing.screen)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                    items(otherLists, key = { it.id }) { list ->
                        SwipeableListCard(
                            list = list,
                            deleteColor = deleteColor,
                            onListClick = onListClick,
                            onRequestDelete = { listToDelete = it }
                        )
                    }
                }
            }
        }
    }
}

/** A list card that reveals a delete background on swipe-left and asks to confirm. */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("DEPRECATION")
@Composable
private fun SwipeableListCard(
    list: ShoppingList,
    deleteColor: androidx.compose.ui.graphics.Color,
    onListClick: (String) -> Unit,
    onRequestDelete: (String) -> Unit
) {
    val spacing = PantryHubTheme.spacing
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onRequestDelete(list.id)
            }
            // Never dismiss the row itself: the confirmation dialog decides.
            false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        modifier = Modifier.padding(horizontal = spacing.screen, vertical = 6.dp),
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        deleteColor.copy(alpha = 0.9f),
                        RoundedCornerShape(PantryHubTheme.radius.card)
                    )
                    .padding(horizontal = spacing.xl),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = PantryIcons.Delete,
                    contentDescription = stringResource(R.string.delete_action),
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }
    ) {
        ShoppingListCard(shoppingList = list, onListClick = onListClick)
    }
}

/** Bottom sheet to create a list: name, type (two choice cards) and an optional base list. */
@Composable
private fun CreateListSheet(
    lists: List<ShoppingList>,
    onDismiss: () -> Unit,
    onCreate: (String, ShoppingListType) -> Unit,
    onClone: (String, String, ShoppingListType) -> Unit
) {
    val spacing = PantryHubTheme.spacing
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(ShoppingListType.REGULAR) }
    var cloneSourceId by remember { mutableStateOf<String?>(null) }
    var cloneMenuExpanded by remember { mutableStateOf(false) }
    val cloneSourceName = lists.find { it.id == cloneSourceId }?.name
        ?: stringResource(R.string.clone_none)

    PantrySheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.fab_new_list),
        subtitle = stringResource(R.string.sheet_new_list_subtitle)
    ) {
        PantryFieldLabel(stringResource(R.string.field_name_label))
        Spacer(modifier = Modifier.height(spacing.sm))
        PantryTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = stringResource(R.string.list_name_placeholder),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(22.dp))
        PantryFieldLabel(stringResource(R.string.list_type_label))
        Spacer(modifier = Modifier.height(spacing.sm))
        Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
            PantryChoiceCard(
                title = stringResource(R.string.list_type_regular),
                description = stringResource(R.string.list_type_regular_desc),
                selected = type == ShoppingListType.REGULAR,
                onClick = { type = ShoppingListType.REGULAR },
                modifier = Modifier.weight(1f)
            )
            PantryChoiceCard(
                title = stringResource(R.string.list_type_temporary),
                description = stringResource(R.string.list_type_temporary_desc),
                selected = type == ShoppingListType.TEMPORARY,
                onClick = { type = ShoppingListType.TEMPORARY },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(22.dp))
        PantryFieldLabel(stringResource(R.string.clone_from_label))
        Spacer(modifier = Modifier.height(spacing.sm))
        Box {
            Surface(
                onClick = { cloneMenuExpanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(PantryHubTheme.radius.row),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = spacing.lg),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = cloneSourceName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = PantryIcons.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
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
                lists.forEach { list ->
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

        Spacer(modifier = Modifier.height(spacing.xl))
        PantryButton(
            onClick = {
                val trimmed = name.trim()
                if (trimmed.isNotEmpty()) {
                    val source = cloneSourceId
                    if (source != null) onClone(source, trimmed, type) else onCreate(trimmed, type)
                    onDismiss()
                }
            },
            enabled = name.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = stringResource(R.string.create_list_action),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
