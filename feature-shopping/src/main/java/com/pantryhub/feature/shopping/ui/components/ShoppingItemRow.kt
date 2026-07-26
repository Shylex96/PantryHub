package com.pantryhub.feature.shopping.ui.components

import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryListItem
import com.pantryhub.core.model.shopping.ShoppingListItem

@Composable
fun ShoppingItemRow(
    item: ShoppingListItem,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PantryListItem(
        title = item.product.name,
        subtitle = null,
        leadingContent = {
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onToggle(item.id) }
            )
        },
        modifier = modifier,
        onClick = { onToggle(item.id) }
    )
}
