package com.pantryhub.feature.shopping.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pantryhub.feature.shopping.presentation.ShoppingUiState
import com.pantryhub.feature.shopping.ui.components.ShoppingItemRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingModeScreen(
    state: ShoppingUiState,
    onToggleItem: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentList = state.currentList ?: return
    
    val pendingItems = currentList.items.filter { !it.isCompleted }
    val completedItems = currentList.items.filter { it.isCompleted }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${currentList.name} - Shopping Mode") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding)
        ) {
            item {
                if (pendingItems.isNotEmpty()) {
                    Text(
                        text = "Pending",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            
            items(pendingItems, key = { it.id }) { item ->
                ShoppingItemRow(item = item, onToggle = onToggleItem)
            }

            if (completedItems.isNotEmpty()) {
                item {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = "Completed",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                
                items(completedItems, key = { it.id }) { item ->
                    ShoppingItemRow(item = item, onToggle = onToggleItem)
                }
            }
        }
    }
}
