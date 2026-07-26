package com.pantryhub.feature.shopping.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryCard
import com.pantryhub.core.designsystem.ui.components.PantryListItem
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.model.shopping.ShoppingList

@Composable
fun ShoppingListCard(
    shoppingList: ShoppingList,
    onListClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = PantryHubTheme.spacing

    PantryCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacing.xs)
    ) {
        PantryListItem(
            title = shoppingList.name,
            subtitle = stringResource(R.string.item_count_subtitle, shoppingList.items.size),
            onClick = { onListClick(shoppingList.id) }
        )
    }
}
