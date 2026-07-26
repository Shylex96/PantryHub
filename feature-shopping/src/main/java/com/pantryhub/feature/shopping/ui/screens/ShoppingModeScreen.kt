package com.pantryhub.feature.shopping.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryTopBar
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.feature.shopping.presentation.ShoppingUiState
import com.pantryhub.feature.shopping.ui.components.ShoppingItemRow

@Composable
fun ShoppingModeScreen(
    state: ShoppingUiState,
    onToggleItem: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentList = state.currentList ?: return
    val spacing = PantryHubTheme.spacing
    
    val pendingItems = currentList.items.filter { !it.isCompleted }
    val completedItems = currentList.items.filter { it.isCompleted }

    Scaffold(
        topBar = {
            PantryTopBar(
                title = stringResource(R.string.shopping_mode_title, currentList.name),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = stringResource(R.string.back_description)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = spacing.sm)
        ) {
            if (pendingItems.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.pending_section),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(spacing.lg),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            items(pendingItems, key = { it.id }) { item ->
                ShoppingItemRow(item = item, onToggle = onToggleItem)
            }

            if (completedItems.isNotEmpty()) {
                item {
                    HorizontalDivider(modifier = Modifier.padding(vertical = spacing.md))
                    Text(
                        text = stringResource(R.string.completed_section),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(spacing.lg),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                
                items(completedItems, key = { it.id }) { item ->
                    ShoppingItemRow(item = item, onToggle = onToggleItem)
                }
            }
        }
    }
}
