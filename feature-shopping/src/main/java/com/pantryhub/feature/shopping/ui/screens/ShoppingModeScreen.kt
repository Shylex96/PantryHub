package com.pantryhub.feature.shopping.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.components.PantryTopBar
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.feature.shopping.presentation.ShoppingUiState
import com.pantryhub.feature.shopping.ui.components.ShoppingItemRow

@Composable
fun ShoppingModeScreen(
    state: ShoppingUiState,
    onToggleItem: (String) -> Unit,
    onFinishShopping: (String?, Double?) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentList = state.currentList ?: return
    val spacing = PantryHubTheme.spacing
    
    val pendingItems = currentList.items.filter { !it.isCompleted }
    val completedItems = currentList.items.filter { it.isCompleted }

    var showFinishDialog by remember { mutableStateOf(false) }
    var supermarket by remember { mutableStateOf("") }
    var totalPrice by remember { mutableStateOf("") }

    if (showFinishDialog) {
        AlertDialog(
            onDismissRequest = { showFinishDialog = false },
            title = { Text(stringResource(R.string.finish_shopping_dialog_title)) },
            text = {
                Column {
                    PantryTextField(
                        value = supermarket,
                        onValueChange = { supermarket = it },
                        label = stringResource(R.string.supermarket_label),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(spacing.md))
                    PantryTextField(
                        value = totalPrice,
                        onValueChange = { totalPrice = it },
                        label = stringResource(R.string.total_price_label),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val price = totalPrice.toDoubleOrNull()
                    onFinishShopping(supermarket.ifBlank { null }, price)
                    showFinishDialog = false
                    onBack()
                }) {
                    Text(stringResource(R.string.save_and_finish))
                }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = {
                        onFinishShopping(null, null)
                        showFinishDialog = false
                        onBack()
                    }) {
                        Text(stringResource(R.string.finish_without_saving))
                    }
                    TextButton(onClick = { showFinishDialog = false }) {
                        Text(stringResource(R.string.cancel_action))
                    }
                }
            }
        )
    }

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
                },
                actions = {
                    IconButton(onClick = { showFinishDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Check, 
                            contentDescription = stringResource(R.string.finish_action)
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
