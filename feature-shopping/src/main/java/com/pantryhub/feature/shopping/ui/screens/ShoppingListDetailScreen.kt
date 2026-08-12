package com.pantryhub.feature.shopping.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryButton
import com.pantryhub.core.designsystem.ui.components.PantryItemCard
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

    // Deterministic color per category, based on its position (same as Products).
    val extended = PantryHubTheme.extendedColors
    val categoryPalette = listOf(
        extended.categoryVegetables,
        extended.categoryFruit,
        extended.categoryDairy,
        extended.categoryMeat,
        extended.categoryBakery,
        extended.categoryDrinks,
        extended.categoryFrozen,
        extended.categoryHousehold,
        extended.categoryOther
    )
    val colorForCategory: (String) -> Color = { id ->
        val index = state.categories.indexOfFirst { it.id == id }
        if (index >= 0) categoryPalette[index % categoryPalette.size] else extended.categoryOther
    }

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
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = PantryIcons.Edit,
                            contentDescription = stringResource(R.string.rename_list_dialog_title),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(spacing.xs))
                    IconButton(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = PantryIcons.Delete,
                            contentDescription = stringResource(R.string.delete_action),
                            modifier = Modifier.size(22.dp)
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
                        .height(56.dp)
                ) {
                    Text(
                        text = stringResource(R.string.start_shopping_action),
                        style = MaterialTheme.typography.titleMedium
                    )
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
                // Add bar: an input with a leading "+" plus a solid accent button.
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = spacing.md),
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm)
                ) {
                    // Placeholder (not a floating label) keeps the input a fixed 56dp
                    // height with vertically-centered text, so the button lines up.
                    PantryTextField(
                        value = state.productQuery,
                        onValueChange = { onIntent(ShoppingIntent.UpdateProductQuery(it)) },
                        placeholder = stringResource(R.string.add_product_placeholder),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = PantryIcons.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                    FilledIconButton(
                        onClick = {
                            if (state.productQuery.isNotBlank()) {
                                onAddItem(state.productQuery, 1.0)
                                onIntent(ShoppingIntent.UpdateProductQuery(""))
                            }
                        },
                        modifier = Modifier.size(56.dp),
                        shape = PantryHubTheme.shapes.medium,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            imageVector = PantryIcons.Add,
                            contentDescription = stringResource(R.string.add_item_label)
                        )
                    }
                }

                // Autocomplete suggestions, shown below the input row.
                if (state.suggestions.isNotEmpty()) {
                    Surface(
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

                Spacer(modifier = Modifier.height(spacing.md))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = spacing.xxl)
                ) {
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
                                        // Return false so the row is not dismissed; only
                                        // the favorite state toggles.
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
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(vertical = spacing.xs)
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
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(vertical = spacing.xs)
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
                                        // Nothing shown when there is no active swipe.
                                    }
                                }
                            }
                        ) {
                            val dotColor = item.product.categoryId?.let { colorForCategory(it) }
                                ?: MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            PantryItemCard(
                                modifier = Modifier.padding(vertical = spacing.xs)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(dotColor)
                                )
                                Spacer(modifier = Modifier.width(spacing.md))
                                Text(
                                    text = item.product.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = {
                                        onIntent(
                                            ShoppingIntent.ToggleFavorite(
                                                item.product.id,
                                                !item.product.isFavorite
                                            )
                                        )
                                    },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = if (item.product.isFavorite) {
                                            PantryIcons.Favorite
                                        } else {
                                            PantryIcons.FavoriteBorder
                                        },
                                        contentDescription = null,
                                        tint = if (item.product.isFavorite) {
                                            favoriteColor
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        },
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(spacing.sm))
                                IconButton(
                                    onClick = { onDeleteItem(item.id) },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = PantryIcons.Delete,
                                        contentDescription = stringResource(R.string.delete_action),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
