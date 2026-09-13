package com.pantryhub.feature.shopping.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.LocalPantryToast
import com.pantryhub.core.designsystem.ui.components.PantryBottomCta
import com.pantryhub.core.designsystem.ui.components.PantryButton
import com.pantryhub.core.designsystem.ui.components.PantryDialog
import com.pantryhub.core.designsystem.ui.components.PantryFieldLabel
import com.pantryhub.core.designsystem.ui.components.PantryHeaderIconButton
import com.pantryhub.core.designsystem.ui.components.PantryItemCard
import com.pantryhub.core.designsystem.ui.components.PantryKeyboard
import com.pantryhub.core.designsystem.ui.components.PantryListItem
import com.pantryhub.core.designsystem.ui.components.PantryLoading
import com.pantryhub.core.designsystem.ui.components.PantryProgressBar
import com.pantryhub.core.designsystem.ui.components.PantrySectionLabel
import com.pantryhub.core.designsystem.ui.components.PantrySheet
import com.pantryhub.core.designsystem.ui.components.PantrySwipeRow
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.components.rememberSheetFocusRequester
import com.pantryhub.core.designsystem.ui.components.shake
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.model.shopping.ShoppingListItem
import com.pantryhub.feature.shopping.presentation.ShoppingIntent
import com.pantryhub.feature.shopping.presentation.ShoppingUiState
import com.pantryhub.feature.shopping.ui.components.groupItemsByCategory
import com.pantryhub.feature.shopping.ui.components.isShoppingInProgress

/**
 * List detail (docs/05_Design_System.md §6.3–6.4, §6.7): compact top bar, title block
 * with meta (and progress only while a shop is in progress), add bar, items grouped by
 * category, and a bottom "Start shopping" call-to-action. Delete = swipe left,
 * favorite = swipe right; rows only show the favorite star.
 */
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
    val radius = PantryHubTheme.radius
    val favoriteColor = PantryHubTheme.extendedColors.favorite
    val haptic = LocalHapticFeedback.current
    val toastState = LocalPantryToast.current
    val context = LocalContext.current

    var quickAddShake by remember { mutableIntStateOf(0) }

    fun submitQuickAdd() {
        val name = state.productQuery.trim()
        if (name.isEmpty()) {
            quickAddShake++
            return
        }
        onAddItem(name, 1.0)
        onIntent(ShoppingIntent.UpdateProductQuery(""))
        toastState.show(context.getString(R.string.product_added_toast, name))
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    val total = currentList.items.size
    val completed = currentList.items.count { it.isCompleted }
    val inProgress = currentList.isShoppingInProgress
    val groups = groupItemsByCategory(currentList.items, state.categories)

    var showRenameSheet by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showRenameSheet) {
        RenameListSheet(
            currentName = currentList.name,
            onDismiss = { showRenameSheet = false },
            onRename = { newName ->
                onRenameList(newName)
                toastState.show(context.getString(R.string.list_renamed_toast))
            }
        )
    }

    if (showDeleteConfirm) {
        PantryDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = stringResource(R.string.delete_list_confirm_title),
            confirmButton = {
                TextButton(onClick = {
                    onDeleteList(currentList.id)
                    showDeleteConfirm = false
                    onBack()
                }) {
                    Text(stringResource(R.string.delete_confirm_action))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text(stringResource(R.string.cancel_action))
                }
            }
        ) {
            Text(stringResource(R.string.delete_list_confirm_message, currentList.name))
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (total > 0) {
                // A shop already under way (some items checked) is resumed, not restarted.
                PantryBottomCta(
                    text = stringResource(
                        if (inProgress) R.string.continue_shopping_action else R.string.start_shopping_action
                    ),
                    onClick = onStartShopping,
                    icon = PantryIcons.Cart,
                    count = if (inProgress) total - completed else total
                )
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            PantryLoading()
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = spacing.xl)
        ) {
            // Compact top bar: back on the left, edit + delete on the right.
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacing.lg, vertical = spacing.sm),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PantryHeaderIconButton(
                        icon = PantryIcons.Back,
                        contentDescription = stringResource(R.string.back_description),
                        onClick = onBack
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                        PantryHeaderIconButton(
                            icon = PantryIcons.Edit,
                            contentDescription = stringResource(R.string.rename_list_dialog_title),
                            onClick = { showRenameSheet = true }
                        )
                        PantryHeaderIconButton(
                            icon = PantryIcons.Delete,
                            contentDescription = stringResource(R.string.delete_action),
                            onClick = { showDeleteConfirm = true }
                        )
                    }
                }
            }

            // Title block.
            item {
                Column(modifier = Modifier.padding(horizontal = spacing.screen, vertical = spacing.md)) {
                    Text(
                        text = currentList.name,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when {
                                total == 0 -> stringResource(R.string.list_meta_empty)
                                inProgress -> stringResource(R.string.detail_meta_in_progress, completed, total)
                                else -> pluralStringResource(R.plurals.products_count, total, total)
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (inProgress) {
                            Text(
                                text = "$completed / $total",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    if (inProgress) {
                        Spacer(modifier = Modifier.height(10.dp))
                        PantryProgressBar(progress = completed / total.toFloat())
                    }
                }
            }

            // Add bar: placeholder field with a leading "+" and a solid square button.
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacing.screen, vertical = spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PantryTextField(
                        value = state.productQuery,
                        onValueChange = { onIntent(ShoppingIntent.UpdateProductQuery(it)) },
                        placeholder = stringResource(R.string.add_product_placeholder),
                        singleLine = true,
                        capitalizeFirstLetter = true,
                        leadingIcon = {
                            Icon(
                                imageVector = PantryIcons.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .shake(quickAddShake),
                        keyboardOptions = PantryKeyboard.text.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { submitQuickAdd() })
                    )
                    FilledIconButton(
                        onClick = { submitQuickAdd() },
                        modifier = Modifier.size(56.dp),
                        shape = RoundedCornerShape(radius.row),
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
            }

            // Autocomplete suggestions right under the add bar.
            if (state.suggestions.isNotEmpty()) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = spacing.screen),
                        shape = RoundedCornerShape(radius.row),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh
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

            // Items grouped by category.
            groups.forEach { group ->
                item(key = "header-${group.category?.id ?: "none"}") {
                    Spacer(modifier = Modifier.height(spacing.md))
                    PantrySectionLabel(
                        text = group.category?.name ?: stringResource(R.string.product_no_category),
                        dotColor = group.color,
                        count = group.items.size,
                        modifier = Modifier.padding(horizontal = spacing.screen)
                    )
                    Spacer(modifier = Modifier.height(spacing.xs))
                }
                items(group.items, key = { it.id }) { item ->
                    SwipeableItemRow(
                        item = item,
                        dotColor = group.color,
                        favoriteColor = favoriteColor,
                        onToggleFavorite = {
                            onIntent(
                                ShoppingIntent.ToggleFavorite(item.product.id, !item.product.isFavorite)
                            )
                        },
                        onDelete = { onDeleteItem(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SwipeableItemRow(
    item: ShoppingListItem,
    dotColor: Color,
    favoriteColor: Color,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    PantrySwipeRow(
        onDelete = onDelete,
        onFavorite = onToggleFavorite,
        modifier = Modifier.padding(horizontal = spacing.screen, vertical = spacing.xs)
    ) {
        PantryItemCard {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(dotColor, CircleShape)
            )
            Spacer(modifier = Modifier.width(spacing.md))
            Text(
                text = item.product.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onToggleFavorite, modifier = Modifier.size(40.dp)) {
                Icon(
                    imageVector = if (item.product.isFavorite) PantryIcons.Favorite else PantryIcons.FavoriteBorder,
                    contentDescription = stringResource(
                        if (item.product.isFavorite) R.string.unfavorite_action else R.string.favorite_action
                    ),
                    tint = if (item.product.isFavorite) favoriteColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/** Bottom sheet to rename the current list. */
@Composable
private fun RenameListSheet(
    currentName: String,
    onDismiss: () -> Unit,
    onRename: (String) -> Unit
) {
    val spacing = PantryHubTheme.spacing
    val haptic = LocalHapticFeedback.current
    // TextFieldValue (remembered once, never rebuilt) so the caret opens at the end of the
    // existing name instead of before it.
    var name by remember {
        mutableStateOf(TextFieldValue(currentName, TextRange(currentName.length)))
    }
    val focusRequester = rememberSheetFocusRequester()
    var shakeTrigger by remember { mutableIntStateOf(0) }

    fun submit() {
        val trimmed = name.text.trim()
        if (trimmed.isEmpty()) {
            shakeTrigger++
            return
        }
        onRename(trimmed)
        onDismiss()
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    PantrySheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.rename_list_dialog_title)
    ) {
        PantryFieldLabel(stringResource(R.string.field_name_label))
        Spacer(modifier = Modifier.height(spacing.sm))
        PantryTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = stringResource(R.string.list_name_placeholder),
            singleLine = true,
            capitalizeFirstLetter = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .shake(shakeTrigger),
            keyboardOptions = PantryKeyboard.text.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { submit() })
        )
        Spacer(modifier = Modifier.height(spacing.xl))
        PantryButton(
            onClick = { submit() },
            enabled = name.text.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = stringResource(R.string.save_action),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
