package com.pantryhub.feature.shopping.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryBottomCta
import com.pantryhub.core.designsystem.ui.components.PantryButton
import com.pantryhub.core.designsystem.ui.components.PantryFieldLabel
import com.pantryhub.core.designsystem.ui.components.PantryHeaderIconButton
import com.pantryhub.core.designsystem.ui.components.PantryProgressBar
import com.pantryhub.core.designsystem.ui.components.PantrySectionLabel
import com.pantryhub.core.designsystem.ui.components.PantrySheet
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.feature.shopping.presentation.ShoppingUiState
import com.pantryhub.feature.shopping.ui.components.ShoppingItemRow
import com.pantryhub.feature.shopping.ui.components.categoryDotColor
import com.pantryhub.feature.shopping.ui.components.groupItemsByCategory

/**
 * Shopping mode (docs/05_Design_System.md §6.7): centred "Shopping mode" label in a
 * compact top bar, list name, big "3 of 8 in the cart" counter with percentage and a
 * 6dp progress bar, pending items grouped by category, completed items in a quieter
 * section below, and a bottom "Finish shopping" call-to-action that opens the finish
 * sheet (optional supermarket + total for the purchase history).
 */
@Composable
fun ShoppingModeScreen(
    state: ShoppingUiState,
    onToggleItem: (String) -> Unit,
    onFinishShopping: (String?, Double?) -> Unit,
    onFinished: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // When the session finishes (list reset or deleted), leave shopping mode and
    // return to the shopping lists screen. Registered before the early return below
    // so it still fires even though currentList becomes null on finish.
    LaunchedEffect(state.shoppingFinished) {
        if (state.shoppingFinished) {
            onFinished()
        }
    }

    val currentList = state.currentList ?: return
    val spacing = PantryHubTheme.spacing

    val total = currentList.items.size
    val completedItems = currentList.items.filter { it.isCompleted }
    val completed = completedItems.size
    val progress = if (total > 0) completed.toFloat() / total else 0f
    val percent = (progress * 100).toInt()
    val pendingGroups = groupItemsByCategory(
        items = currentList.items.filter { !it.isCompleted },
        categories = state.categories
    )

    var showFinishSheet by remember { mutableStateOf(false) }

    if (showFinishSheet) {
        FinishShoppingSheet(
            onDismiss = { showFinishSheet = false },
            onFinish = { supermarket, price ->
                onFinishShopping(supermarket, price)
                showFinishSheet = false
                // Navigation happens via the shoppingFinished event once the
                // finish work completes (see LaunchedEffect above).
            }
        )
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            PantryBottomCta(
                text = stringResource(R.string.finish_shopping_action),
                onClick = { showFinishSheet = true },
                icon = PantryIcons.Check
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = spacing.xl)
        ) {
            // Compact top bar: back on the left, centred mode label.
            item(key = "top-bar") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacing.lg, vertical = spacing.sm)
                ) {
                    PantryHeaderIconButton(
                        icon = PantryIcons.Back,
                        contentDescription = stringResource(R.string.back_description),
                        onClick = onBack,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    Text(
                        text = stringResource(R.string.shopping_mode_label).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.2.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // Title + counter + progress.
            item(key = "header") {
                Column(
                    modifier = Modifier.padding(
                        start = spacing.screen,
                        end = spacing.screen,
                        top = spacing.sm,
                        bottom = spacing.lg
                    )
                ) {
                    Text(
                        text = currentList.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(spacing.md))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = completed.toString(),
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = 40.sp,
                                lineHeight = 44.sp
                            ),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(spacing.sm))
                        Text(
                            text = stringResource(R.string.shopping_counter_suffix, total),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 6.dp)
                        )
                        Text(
                            text = "$percent%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(spacing.md))
                    PantryProgressBar(progress = progress, height = 6.dp)
                }
            }

            // Pending items, grouped by category.
            pendingGroups.forEach { group ->
                val groupKey = group.category?.id ?: "uncategorized"
                item(key = "header-$groupKey") {
                    PantrySectionLabel(
                        text = group.category?.name ?: stringResource(R.string.product_no_category),
                        dotColor = group.color,
                        count = group.items.size,
                        modifier = Modifier
                            .padding(horizontal = spacing.screen)
                            .padding(top = spacing.md, bottom = spacing.sm)
                    )
                }
                items(group.items, key = { it.id }) { item ->
                    ShoppingItemRow(
                        item = item,
                        onToggle = onToggleItem,
                        dotColor = group.color,
                        modifier = Modifier
                            .animateItem()
                            .padding(horizontal = spacing.screen, vertical = spacing.xs)
                    )
                }
            }

            // Completed items, quieter, below everything pending.
            if (completedItems.isNotEmpty()) {
                item(key = "header-completed") {
                    PantrySectionLabel(
                        text = stringResource(R.string.completed_section),
                        count = completedItems.size,
                        modifier = Modifier
                            .padding(horizontal = spacing.screen)
                            .padding(top = spacing.lg, bottom = spacing.sm)
                    )
                }
                items(completedItems, key = { it.id }) { item ->
                    ShoppingItemRow(
                        item = item,
                        onToggle = onToggleItem,
                        dotColor = categoryDotColor(item.product.categoryId, state.categories),
                        modifier = Modifier
                            .animateItem()
                            .padding(horizontal = spacing.screen, vertical = spacing.xs)
                    )
                }
            }
        }
    }
}

/**
 * Finish flow. "Save and finish" needs both fields (that is what goes to the purchase
 * history); "Just finish" ends the shop without recording anything.
 */
@Composable
private fun FinishShoppingSheet(
    onDismiss: () -> Unit,
    onFinish: (supermarket: String?, price: Double?) -> Unit
) {
    val spacing = PantryHubTheme.spacing
    var supermarket by remember { mutableStateOf("") }
    var totalPrice by remember { mutableStateOf("") }

    val isSupermarketFilled = supermarket.isNotBlank()
    val isPriceFilled = totalPrice.isNotBlank()
    val parsedPrice = totalPrice.replace(',', '.').toDoubleOrNull()
    val isSaveEnabled = isSupermarketFilled && parsedPrice != null
    val showValidation = (isSupermarketFilled || isPriceFilled) && !isSaveEnabled

    PantrySheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.finish_shopping_dialog_title),
        subtitle = stringResource(R.string.sheet_finish_shopping_subtitle)
    ) {
        PantryFieldLabel(stringResource(R.string.field_supermarket_label))
        Spacer(modifier = Modifier.height(spacing.sm))
        PantryTextField(
            value = supermarket,
            onValueChange = { supermarket = it },
            placeholder = stringResource(R.string.field_supermarket_placeholder),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(spacing.lg))
        PantryFieldLabel(stringResource(R.string.field_total_label))
        Spacer(modifier = Modifier.height(spacing.sm))
        PantryTextField(
            value = totalPrice,
            onValueChange = { totalPrice = it },
            placeholder = stringResource(R.string.field_total_placeholder),
            singleLine = true,
            isError = isPriceFilled && parsedPrice == null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        if (showValidation) {
            Text(
                text = stringResource(R.string.finish_validation_error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = spacing.xs)
            )
        }
        Spacer(modifier = Modifier.height(spacing.xl))
        PantryButton(
            onClick = { onFinish(supermarket.trim(), parsedPrice) },
            enabled = isSaveEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_and_finish))
        }
        Spacer(modifier = Modifier.height(spacing.sm))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(onClick = { onFinish(null, null) }) {
                Text(stringResource(R.string.finish_without_saving))
            }
        }
    }
}
