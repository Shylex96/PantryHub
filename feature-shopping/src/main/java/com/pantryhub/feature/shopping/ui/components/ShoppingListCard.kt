package com.pantryhub.feature.shopping.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.ui.components.PantryCard
import com.pantryhub.core.designsystem.ui.components.PantryListItem
import com.pantryhub.core.model.shopping.ShoppingList

@Composable
fun ShoppingListCard(
    shoppingList: ShoppingList,
    onListClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PantryCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        PantryListItem(
            title = shoppingList.name,
            subtitle = "${shoppingList.items.size} items",
            onClick = { onListClick(shoppingList.id) }
        )
    }
}
