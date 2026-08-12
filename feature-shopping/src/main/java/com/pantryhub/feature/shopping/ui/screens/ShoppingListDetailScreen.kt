package com.pantryhub.feature.shopping.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import com.pantryhub.feature.shopping.presentation.ShoppingIntent
import com.pantryhub.feature.shopping.presentation.ShoppingUiState

@OptIn(ExperimentalMaterial3Api::class)
// SwipeToDismiss confirmValueChange is deprecated without a drop-in replacement;
// migration to dynamic anchors is tracked for the polish sprint.
@Suppress("DEPRECATION")
@Composable
fun ShoppingListDetailScreen(
    state: ShoppingUiState,
    onIntent: (ShoppingIntent) -> Unit,
    onAddItem: (String, Double) -> Unit,
    onDeleteItem: (String) -> Unit,
    onDeleteList: (String) -> Unit,
    onRenameList: (String) -> Unit,
    onStartShopping: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentList = state.currentList ?: return
    val spacing = PantryHubTheme.spacing
    val favoriteColor = PantryHubTheme.extendedColors.favorite
    val onFavoriteColor = PantryHubTheme.extendedColors.onFavorite
    val deleteColor = MaterialTheme.colorScheme.error

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
                    IconButton(
                        onClick = {
                            listNameBuffer = currentList.name
                            showRenameDialog = true
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = PantryIcons.Edit,
                            contentDescription = stringResource(R.string.rename_list_dialog_title),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(spacing.sm))
                    IconButton(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = PantryIcons.Delete,
                            contentDescription = stringResource(R.string.delete_action),
                            modifier = Modifier.size(18.dp)
                        )
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
                // Symmetrical Search/Add Bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = spacing.md),
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm)
                ) {
                    Column(modifier = Modifier.weight(0.9f)) {
                        PantryTextField(
                            value = state.productQuery,
                            onValueChange = { onIntent(ShoppingIntent.UpdateProductQuery(it)) },
                            label = stringResource(R.string.add_item_label),
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (state.suggestions.isNotEmpty()) {
                            androidx.compose.material3.Surface(
                                modifier = Modifier.fillMaxWidth(),
                                tonalElevation = PantryHubTheme.elevations.medium,
                                shape = PantryHubTheme.shapes.small
                            ) {
                                Column {
                                    state.suggestions.forEach { suggestion ->
                                        PantryListItem(
                                            title = suggestion.name,
                                            onClick = {
                                                onAddItem(suggestion.name, 1.0)
                                                onIntent(ShoppingIntent.UpdateProductQuery(""))
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    IconButton(
                        onClick = {
                            if (state.productQuery.isNotBlank()) {
                                onAddItem(state.productQuery, 1.0)
                                onIntent(ShoppingIntent.UpdateProductQuery(""))
                            }
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = PantryIcons.Add,
                            contentDescription = stringResource(R.string.add_item_label),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(spacing.md))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(currentList.items, key = { it.id }) { item ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = {
                                when (it) {
                                    SwipeToDismissBoxValue.EndToStart -> {
                                        // Swiping to the left deletes the item.
                                        onDeleteItem(item.id)
                                        true
                                    }

                                    SwipeToDismissBoxValue.StartToEnd -> {
                                        // Swiping to the right marks the item as favorite.
                                        onIntent(
                                            ShoppingIntent.ToggleFavorite(
                                                item.product.id,
                                                !item.product.isFavorite
                                            )
                                        )
                                        // I return false because I don't want the item to be dismissed from the list.
                                        // I only toggle its favorite state and let it return to its original position.
                                        false
                                    }

                                    else -> false
                                }
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = true,
                            enableDismissFromEndToStart = true,
                            backgroundContent = {
                                val progress = dismissState.progress
                                val colorAlpha = (progress * 2f).coerceAtMost(1f)
                                val iconProgress = ((progress - 0.20f) / 0.80f).coerceAtLeast(0f)
                                val scale = 0.5f + (iconProgress * 0.5f).coerceAtMost(0.5f)

                                when (dismissState.dismissDirection) {
                                    SwipeToDismissBoxValue.EndToStart -> {
                                        // I display a red background with the delete icon aligned to the right.
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    deleteColor.copy(alpha = colorAlpha),
                                                    PantryHubTheme.shapes.medium
                                                )
                                                .padding(horizontal = spacing.xl),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            Icon(
                                                imageVector = PantryIcons.Delete,
                                                contentDescription = stringResource(R.string.delete_action),
                                                modifier = Modifier.scale(scale)
                                            )
                                        }
                                    }

                                    SwipeToDismissBoxValue.StartToEnd -> {
                                        // I display a gold background with the favorite star aligned to the left.
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    favoriteColor.copy(alpha = colorAlpha),
                                                    PantryHubTheme.shapes.medium
                                                )
                                                .padding(horizontal = spacing.xl),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            Icon(
                                                imageVector = PantryIcons.Favorite,
                                                contentDescription = null,
                                                tint = onFavoriteColor,
                                                modifier = Modifier.scale(scale)
                                            )
                                        }
                                    }

                                    SwipeToDismissBoxValue.Settled -> {
                                        // I don't show anything when there is no active swipe.
                                    }
                                }
                            }
                        ) {
                            PantryListItem(
                                title = item.product.name,
                                subtitle = null,
                                trailingContent = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = {
                                                onIntent(
                                                    ShoppingIntent.ToggleFavorite(
                                                        item.product.id,
                                                        !item.product.isFavorite
                                                    )
                                                )
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (item.product.isFavorite) {
                                                    PantryIcons.Favorite
                                                } else {
                                                    PantryIcons.FavoriteBorder
                                                },
                                                contentDescription = null,
                                                // I previously used Color.Unspecified for the "not favorite" state.
                                                // On vectors without a base color, this could make the icon invisible.
                                                // I now use an explicit theme color instead.
                                                tint = if (item.product.isFavorite) {
                                                    favoriteColor
                                                } else {
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                                },
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(spacing.sm))
                                        IconButton(
                                            onClick = { onDeleteItem(item.id) },
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
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}