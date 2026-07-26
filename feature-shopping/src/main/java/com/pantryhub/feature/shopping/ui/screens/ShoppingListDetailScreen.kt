package com.pantryhub.feature.shopping.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryButton
import com.pantryhub.core.designsystem.ui.components.PantryListItem
import com.pantryhub.core.designsystem.ui.components.PantryLoading
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.components.PantryTopBar
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.feature.shopping.presentation.ShoppingUiState

@Composable
fun ShoppingListDetailScreen(
    state: ShoppingUiState,
    onAddItem: (String, Double) -> Unit,
    onDeleteItem: (String) -> Unit,
    onStartShopping: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentList = state.currentList ?: return
    var newItemName by remember { mutableStateOf("") }
    val spacing = PantryHubTheme.spacing

    Scaffold(
        topBar = {
            PantryTopBar(
                title = currentList.name,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = stringResource(R.string.back_description)
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
                            imageVector = Icons.Default.Add, 
                            contentDescription = stringResource(R.string.create_list_description)
                        )
                    }
                }

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(currentList.items, key = { it.id }) { item ->
                        PantryListItem(
                            title = item.product.name,
                            subtitle = stringResource(R.string.item_quantity_subtitle, item.quantity),
                            trailingContent = {
                                IconButton(onClick = { onDeleteItem(item.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = null)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
