package com.pantryhub.feature.shopping.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryBadge
import com.pantryhub.core.designsystem.ui.components.PantryListCard
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.model.shopping.ShoppingList
import com.pantryhub.core.model.shopping.ShoppingListType

/** A list is "in progress" once at least one item has been checked in shopping mode. */
val ShoppingList.isShoppingInProgress: Boolean
    get() = items.any { it.isCompleted }

/**
 * Maps a [ShoppingList] onto the design-system [PantryListCard]: tinted tile by type,
 * a meta line that only talks about "the cart" once shopping has started, a progress bar
 * only while in progress, and a "One-off" badge for provisional lists
 * (docs/05_Design_System.md §6.2).
 */
@Composable
fun ShoppingListCard(
    shoppingList: ShoppingList,
    onListClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val total = shoppingList.items.size
    val completed = shoppingList.items.count { it.isCompleted }
    val inProgress = shoppingList.isShoppingInProgress
    val isTemporary = shoppingList.type == ShoppingListType.TEMPORARY

    val meta = when {
        total == 0 -> stringResource(R.string.list_meta_empty)
        inProgress -> stringResource(R.string.list_meta_in_progress, completed, total)
        else -> pluralStringResource(R.plurals.products_count, total, total)
    }

    PantryListCard(
        title = shoppingList.name,
        meta = meta,
        icon = if (isTemporary) PantryIcons.Schedule else PantryIcons.Lists,
        tileColor = if (isTemporary) {
            MaterialTheme.colorScheme.tertiaryContainer
        } else {
            MaterialTheme.colorScheme.primaryContainer
        },
        onTileColor = if (isTemporary) {
            MaterialTheme.colorScheme.onTertiaryContainer
        } else {
            MaterialTheme.colorScheme.onPrimaryContainer
        },
        onClick = { onListClick(shoppingList.id) },
        modifier = modifier,
        progress = if (inProgress) completed / total.toFloat() else null,
        progressColor = if (isTemporary) {
            MaterialTheme.colorScheme.tertiary
        } else {
            MaterialTheme.colorScheme.primary
        },
        badge = if (isTemporary) {
            {
                PantryBadge(
                    text = stringResource(R.string.list_badge_temporary),
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        } else {
            null
        }
    )
}
