package com.pantryhub.feature.products.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryButton
import com.pantryhub.core.designsystem.ui.components.PantryDialog
import com.pantryhub.core.designsystem.ui.components.PantryEmptyState
import com.pantryhub.core.designsystem.ui.components.PantryFieldLabel
import com.pantryhub.core.designsystem.ui.components.PantryHeaderIconButton
import com.pantryhub.core.designsystem.ui.components.PantryItemCard
import com.pantryhub.core.designsystem.ui.components.PantryLoading
import com.pantryhub.core.designsystem.ui.components.PantryScreenHeader
import com.pantryhub.core.designsystem.ui.components.PantrySectionLabel
import com.pantryhub.core.designsystem.ui.components.PantrySheet
import com.pantryhub.core.designsystem.ui.components.PantrySwipeRow
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.designsystem.ui.theme.uncategorizedDotColor
import com.pantryhub.core.model.category.Category
import com.pantryhub.feature.products.presentation.CategoriesViewModel
import com.pantryhub.feature.products.ui.components.categoryColor

/**
 * Categories screen (docs/04_UX_Guidelines.md "Category Browsing"): reached from the
 * Products header. Add bar on top, one row per category with its color dot and product
 * count; tap a row to rename or delete it, or swipe left to delete. Products in a deleted
 * category are kept and become uncategorised.
 */
@Composable
fun CategoriesScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: CategoriesViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    val spacing = PantryHubTheme.spacing
    val categories = state.rows.map { it.category }

    var input by remember { mutableStateOf("") }
    var editing by remember { mutableStateOf<Category?>(null) }
    var pendingDelete by remember { mutableStateOf<Category?>(null) }

    fun submitNew() {
        val name = input.trim()
        if (name.isNotEmpty()) {
            viewModel.create(name)
            input = ""
        }
    }

    val editingCategory = editing
    if (editingCategory != null) {
        EditCategorySheet(
            category = editingCategory,
            onRename = { name ->
                viewModel.rename(editingCategory.id, name)
                editing = null
            },
            onDelete = {
                editing = null
                pendingDelete = editingCategory
            },
            onDismiss = { editing = null }
        )
    }

    val toDelete = pendingDelete
    if (toDelete != null) {
        PantryDialog(
            onDismissRequest = { pendingDelete = null },
            title = stringResource(R.string.delete_category_confirm_title),
            confirmButton = {
                TextButton(onClick = {
                    viewModel.delete(toDelete)
                    pendingDelete = null
                }) {
                    Text(stringResource(R.string.delete_confirm_action))
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) {
                    Text(stringResource(R.string.cancel_action))
                }
            }
        ) {
            Text(stringResource(R.string.delete_category_confirm_message, toDelete.name))
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = spacing.xxl)
        ) {
            // Compact top bar (docs/05 §6.6): back only; the title lives in the header.
            item(key = "top-bar") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacing.lg, vertical = spacing.sm)
                ) {
                    PantryHeaderIconButton(
                        icon = PantryIcons.Back,
                        contentDescription = stringResource(R.string.back_description),
                        onClick = onBack
                    )
                }
            }
            item(key = "header") {
                PantryScreenHeader(
                    title = stringResource(R.string.category_manager_title),
                    subtitle = if (state.rows.isEmpty()) {
                        stringResource(R.string.sheet_categories_subtitle)
                    } else {
                        pluralStringResource(R.plurals.categories_count, state.rows.size, state.rows.size)
                    }
                )
            }

            // Add bar: placeholder field + solid square "+" (docs/05 §6.4).
            item(key = "add-bar") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacing.screen)
                        .padding(top = spacing.lg)
                ) {
                    PantryTextField(
                        value = input,
                        onValueChange = { input = it },
                        placeholder = stringResource(R.string.category_name_placeholder),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    FilledIconButton(
                        onClick = { submitNew() },
                        modifier = Modifier.size(56.dp),
                        shape = RoundedCornerShape(PantryHubTheme.radius.row),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            imageVector = PantryIcons.Add,
                            contentDescription = stringResource(R.string.add_category_action)
                        )
                    }
                }
            }

            when {
                state.isLoading -> item(key = "loading") {
                    Box(modifier = Modifier.padding(top = spacing.xxl)) { PantryLoading() }
                }

                state.rows.isEmpty() -> item(key = "empty") {
                    PantryEmptyState(
                        title = stringResource(R.string.category_empty),
                        description = stringResource(R.string.categories_empty_desc),
                        icon = PantryIcons.Category,
                        modifier = Modifier.padding(top = spacing.xl)
                    )
                }

                else -> {
                    item(key = "label") {
                        PantrySectionLabel(
                            text = stringResource(R.string.categories_section_all),
                            count = state.rows.size,
                            modifier = Modifier
                                .padding(horizontal = spacing.screen)
                                .padding(top = spacing.xl, bottom = spacing.sm)
                        )
                    }
                    items(state.rows, key = { it.category.id }) { row ->
                        PantrySwipeRow(
                            onDelete = { pendingDelete = row.category },
                            dismissOnDelete = false,
                            modifier = Modifier
                                .animateItem()
                                .padding(horizontal = spacing.screen, vertical = spacing.xs)
                        ) {
                            PantryItemCard(onClick = { editing = row.category }) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(categoryColor(row.category, categories), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(spacing.md))
                                Text(
                                    text = row.category.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = pluralStringResource(
                                        R.plurals.products_count, row.productCount, row.productCount
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(spacing.sm))
                                Icon(
                                    imageVector = PantryIcons.ChevronRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    if (state.uncategorizedCount > 0) {
                        item(key = "uncategorized") {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(horizontal = spacing.screen)
                                    .padding(top = spacing.lg)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(uncategorizedDotColor(), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(spacing.sm))
                                Text(
                                    text = pluralStringResource(
                                        R.plurals.uncategorized_products_hint,
                                        state.uncategorizedCount,
                                        state.uncategorizedCount
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    item(key = "hint") {
                        Text(
                            text = stringResource(R.string.categories_hint),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                                .padding(horizontal = spacing.screen)
                                .padding(top = spacing.md)
                        )
                    }
                }
            }
        }
    }
}

/** Rename a category, or delete it (confirmation follows). */
@Composable
private fun EditCategorySheet(
    category: Category,
    onRename: (String) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    var name by remember { mutableStateOf(category.name) }

    PantrySheet(
        onDismissRequest = onDismiss,
        title = category.name,
        subtitle = stringResource(R.string.sheet_edit_category_subtitle)
    ) {
        PantryFieldLabel(stringResource(R.string.field_name_label))
        Spacer(modifier = Modifier.height(spacing.sm))
        PantryTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = stringResource(R.string.category_name_placeholder),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(spacing.xl))
        PantryButton(
            onClick = { onRename(name.trim()) },
            enabled = name.isNotBlank() && name.trim() != category.name,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
        Spacer(modifier = Modifier.height(spacing.sm))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(onClick = onDelete) {
                Text(
                    text = stringResource(R.string.delete_category_action),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
