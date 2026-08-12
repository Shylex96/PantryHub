package com.pantryhub.feature.shopping.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryCard
import com.pantryhub.core.designsystem.ui.components.PantryListItem
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.model.shopping.ShoppingList
import com.pantryhub.core.model.shopping.ShoppingListType

@Composable
fun ShoppingListCard(
    shoppingList: ShoppingList,
    onListClick: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = PantryHubTheme.spacing

    // Subtitle shows the item count plus a short type badge for every list
    // (e.g. "3 items · One-off" / "3 items · Regular") so the user can tell a
    // provisional list from a regular one at a glance without opening it.
    val itemCountText = stringResource(R.string.item_count_subtitle, shoppingList.items.size)
    val typeBadge = when (shoppingList.type) {
        ShoppingListType.TEMPORARY -> stringResource(R.string.list_badge_temporary)
        else -> stringResource(R.string.list_badge_regular)
    }
    val subtitle = "$itemCountText · $typeBadge"

    PantryCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacing.xs)
    ) {
        PantryListItem(
            title = shoppingList.name,
            subtitle = subtitle,
            onClick = { onListClick(shoppingList.id) },
            trailingContent = {
                IconButton(
                    onClick = { onDelete(shoppingList.id) },
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
