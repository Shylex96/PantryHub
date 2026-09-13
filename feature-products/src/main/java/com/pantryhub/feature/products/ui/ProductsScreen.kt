package com.pantryhub.feature.products.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryBottomCta
import com.pantryhub.core.designsystem.ui.components.PantryCheckbox
import com.pantryhub.core.designsystem.ui.components.PantryDialog
import com.pantryhub.core.designsystem.ui.components.LocalPantryToast
import com.pantryhub.core.designsystem.ui.components.PantryEmptyState
import com.pantryhub.core.designsystem.ui.components.PantryExtendedFab
import com.pantryhub.core.designsystem.ui.components.PantryHeaderIconButton
import com.pantryhub.core.designsystem.ui.components.PantryItemCard
import com.pantryhub.core.designsystem.ui.components.PantryLoading
import com.pantryhub.core.designsystem.ui.components.PantryScreenHeader
import com.pantryhub.core.designsystem.ui.components.PantrySearchField
import com.pantryhub.core.designsystem.ui.components.PantrySectionLabel
import com.pantryhub.core.designsystem.ui.components.PantrySheet
import com.pantryhub.core.designsystem.ui.components.PantrySwipeRow
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.designsystem.ui.theme.uncategorizedDotColor
import com.pantryhub.core.model.product.Product
import com.pantryhub.feature.products.presentation.ProductFilter
import com.pantryhub.feature.products.presentation.ProductsIntent
import com.pantryhub.feature.products.presentation.ProductsUiState
import com.pantryhub.feature.products.ui.components.AssignCategorySheet
import com.pantryhub.feature.products.ui.components.EditProductSheet
import com.pantryhub.feature.products.ui.components.NewProductSheet
import com.pantryhub.feature.products.ui.components.ProductFilterSheet
import com.pantryhub.feature.products.ui.components.ProductGroupHeader
import com.pantryhub.feature.products.ui.components.productDotColor
import com.pantryhub.feature.products.ui.components.rememberProductGroups

/**
 * Products tab (docs/04_UX_Guidelines.md "Category Browsing", docs/05 §6.1, §6.7):
 * large header with live counts, a Categories button and a filter button, permanent
 * search pill, products grouped by category (Favorites first, "No category" last),
 * extended FAB to create a product. Tap = edit · long press = actions · swipe left =
 * delete · swipe right = favorite. A **selection mode** (long press → "Select several",
 * or "Organise" on the No-category group) lets the user move many products to a
 * category at once or delete them.
 */
@Composable
fun ProductsScreen(
    state: ProductsUiState,
    onIntent: (ProductsIntent) -> Unit,
    onOpenCategories: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = PantryHubTheme.spacing
    val favoriteColor = PantryHubTheme.extendedColors.favorite

    var showFilterSheet by remember { mutableStateOf(false) }
    var showNewProductSheet by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<Product?>(null) }
    var actionProduct by remember { mutableStateOf<Product?>(null) }
    val toastState = LocalPantryToast.current
    val context = LocalContext.current

    // Selection mode is pure UI state: which ids are ticked, and whether the mode is on
    // (it can be on with nothing ticked yet).
    var selecting by remember { mutableStateOf(false) }
    var selectedIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showAssignSheet by remember { mutableStateOf(false) }
    var confirmBulkDelete by remember { mutableStateOf(false) }

    fun exitSelection() {
        selecting = false
        selectedIds = emptySet()
    }
    fun toggleSelected(id: String) {
        selectedIds = if (id in selectedIds) selectedIds - id else selectedIds + id
    }

    BackHandler(enabled = selecting) { exitSelection() }

    val groups = rememberProductGroups(
        products = state.products,
        categories = state.categories,
        filter = state.filter,
        sort = state.sort
    )
    val visibleProducts = groups.flatMap { it.products }
    val visibleCount = visibleProducts.size

    if (showFilterSheet) {
        ProductFilterSheet(
            products = state.products,
            categories = state.categories,
            currentFilter = state.filter,
            currentSort = state.sort,
            onApply = { filter, sort ->
                onIntent(ProductsIntent.ApplyFilter(filter, sort))
                showFilterSheet = false
            },
            onManageCategories = {
                showFilterSheet = false
                onOpenCategories()
            },
            onDismiss = { showFilterSheet = false }
        )
    }

    if (showNewProductSheet) {
        NewProductSheet(
            categories = state.categories,
            onCreate = { name, categoryId ->
                onIntent(ProductsIntent.CreateProduct(name, categoryId))
                showNewProductSheet = false
                toastState.show(context.getString(R.string.product_added_toast, name))
            },
            onDismiss = { showNewProductSheet = false }
        )
    }

    val editing = editingProduct
    if (editing != null) {
        EditProductSheet(
            product = editing,
            categories = state.categories,
            onSave = { name, categoryId, aliases ->
                onIntent(ProductsIntent.UpdateProductDetails(editing.id, name, categoryId, aliases))
                editingProduct = null
                toastState.show(context.getString(R.string.product_updated_toast))
            },
            onDismiss = { editingProduct = null }
        )
    }

    val acting = actionProduct
    if (acting != null) {
        ProductActionsSheet(
            product = acting,
            onEdit = {
                actionProduct = null
                editingProduct = acting
            },
            onToggleFavorite = {
                onIntent(ProductsIntent.ToggleFavorite(acting.id, !acting.isFavorite))
                actionProduct = null
            },
            onSelectSeveral = {
                actionProduct = null
                selecting = true
                selectedIds = setOf(acting.id)
            },
            onDelete = {
                onIntent(ProductsIntent.DeleteProduct(acting.id))
                actionProduct = null
            },
            onDismiss = { actionProduct = null }
        )
    }

    if (showAssignSheet) {
        AssignCategorySheet(
            productCount = selectedIds.size,
            categories = state.categories,
            onAssign = { categoryId ->
                onIntent(ProductsIntent.AssignCategory(selectedIds, categoryId))
                showAssignSheet = false
                exitSelection()
            },
            onDismiss = { showAssignSheet = false }
        )
    }

    if (confirmBulkDelete) {
        PantryDialog(
            onDismissRequest = { confirmBulkDelete = false },
            title = stringResource(R.string.delete_products_confirm_title),
            confirmButton = {
                TextButton(onClick = {
                    onIntent(ProductsIntent.DeleteProducts(selectedIds))
                    confirmBulkDelete = false
                    exitSelection()
                }) {
                    Text(stringResource(R.string.delete_confirm_action))
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmBulkDelete = false }) {
                    Text(stringResource(R.string.cancel_action))
                }
            }
        ) {
            Text(
                pluralStringResource(
                    R.plurals.delete_products_confirm_message, selectedIds.size, selectedIds.size
                )
            )
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            if (!selecting) {
                PantryExtendedFab(
                    text = stringResource(R.string.fab_new_product),
                    icon = PantryIcons.Add,
                    onClick = { showNewProductSheet = true }
                )
            }
        },
        bottomBar = {
            if (selecting) {
                PantryBottomCta(
                    text = stringResource(R.string.assign_category_title),
                    onClick = { showAssignSheet = true },
                    icon = PantryIcons.Category,
                    count = selectedIds.size.takeIf { it > 0 },
                    enabled = selectedIds.isNotEmpty()
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(top = spacing.lg, bottom = 96.dp)
        ) {
            if (selecting) {
                item(key = "selection-bar") {
                    SelectionBar(
                        selectedCount = selectedIds.size,
                        allSelected = selectedIds.size == visibleCount && visibleCount > 0,
                        onClose = { exitSelection() },
                        onSelectAll = {
                            selectedIds = if (selectedIds.size == visibleCount) {
                                emptySet()
                            } else {
                                visibleProducts.map { it.id }.toSet()
                            }
                        },
                        onDelete = { confirmBulkDelete = true }
                    )
                }
            } else {
                item(key = "header") {
                    PantryScreenHeader(
                        title = stringResource(R.string.nav_products),
                        subtitle = pluralStringResource(
                            R.plurals.products_count, state.products.size, state.products.size
                        ) + " · " + pluralStringResource(
                            R.plurals.categories_count, state.categories.size, state.categories.size
                        ),
                        action = {
                            Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                                PantryHeaderIconButton(
                                    icon = PantryIcons.Category,
                                    contentDescription = stringResource(R.string.category_manager_title),
                                    onClick = onOpenCategories
                                )
                                FilterButton(
                                    active = state.isFilterActive,
                                    onClick = { showFilterSheet = true }
                                )
                            }
                        }
                    )
                }

                item(key = "search") {
                    PantrySearchField(
                        value = state.searchQuery,
                        onValueChange = { onIntent(ProductsIntent.Search(it)) },
                        placeholder = stringResource(R.string.search_products_placeholder),
                        clearContentDescription = stringResource(R.string.clear_search_action),
                        modifier = Modifier.padding(
                            start = spacing.screen,
                            end = spacing.screen,
                            top = spacing.lg
                        )
                    )
                }
            }

            when {
                state.isLoading && state.products.isEmpty() -> item(key = "loading") {
                    Box(modifier = Modifier.padding(top = spacing.xxl)) { PantryLoading() }
                }

                visibleCount == 0 -> item(key = "empty") {
                    val hasQuery = state.searchQuery.isNotBlank()
                    Box(modifier = Modifier.padding(top = spacing.xl)) {
                        when {
                            hasQuery -> PantryEmptyState(
                                title = stringResource(R.string.search_no_results_title),
                                description = stringResource(
                                    R.string.search_no_results_desc, state.searchQuery
                                ),
                                icon = PantryIcons.Search
                            )
                            state.filter != ProductFilter.All -> PantryEmptyState(
                                title = stringResource(R.string.filter_no_results_title),
                                description = stringResource(R.string.filter_no_results_desc),
                                icon = PantryIcons.Filter
                            )
                            else -> PantryEmptyState(
                                title = stringResource(R.string.empty_products_title),
                                description = stringResource(R.string.empty_products_desc),
                                icon = PantryIcons.Products
                            )
                        }
                    }
                }

                else -> groups.forEach { group ->
                    val header = group.header
                    val groupKey = when (header) {
                        ProductGroupHeader.Favorites -> "favorites"
                        is ProductGroupHeader.ByCategory -> header.category.id
                        ProductGroupHeader.Uncategorized -> "uncategorized"
                        ProductGroupHeader.None -> "all"
                    }
                    if (header != ProductGroupHeader.None) {
                        item(key = "header-$groupKey") {
                            // "Organise" on the No-category group: jump straight into
                            // selection with every uncategorised product ticked.
                            val organise: (@Composable () -> Unit)? =
                                if (header == ProductGroupHeader.Uncategorized && !selecting) {
                                    {
                                        TextButton(
                                            onClick = {
                                                selecting = true
                                                selectedIds = group.products.map { it.id }.toSet()
                                            },
                                            contentPadding = PaddingValues(horizontal = spacing.sm)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.organise_action),
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                } else {
                                    null
                                }
                            GroupLabel(
                                header = header,
                                count = group.products.size,
                                action = organise,
                                modifier = Modifier
                                    .padding(horizontal = spacing.screen)
                                    .padding(top = spacing.lg, bottom = spacing.sm)
                            )
                        }
                    } else {
                        item(key = "spacer-flat") { Spacer(modifier = Modifier.height(spacing.md)) }
                    }
                    items(group.products, key = { "$groupKey-${it.id}" }) { product ->
                        val categoryName = product.categoryId?.let { id ->
                            state.categories.firstOrNull { it.id == id }?.name
                        }
                        // Under a category header the category is implied; elsewhere show it.
                        val showCategory = header !is ProductGroupHeader.ByCategory &&
                            header != ProductGroupHeader.Uncategorized
                        val aliasesText = if (product.aliases.isNotEmpty()) {
                            pluralStringResource(
                                R.plurals.aliases_count, product.aliases.size, product.aliases.size
                            )
                        } else {
                            null
                        }
                        val subtitle = listOfNotNull(
                            if (showCategory) categoryName ?: stringResource(R.string.product_no_category) else null,
                            aliasesText
                        ).joinToString(" · ").ifEmpty { null }
                        val dotColor = productDotColor(product.categoryId, state.categories)
                        val rowModifier = Modifier
                            .animateItem()
                            .padding(horizontal = spacing.screen, vertical = spacing.xs)

                        if (selecting) {
                            ProductRow(
                                product = product,
                                subtitle = subtitle,
                                dotColor = dotColor,
                                favoriteColor = favoriteColor,
                                selected = product.id in selectedIds,
                                onClick = { toggleSelected(product.id) },
                                onLongClick = { toggleSelected(product.id) },
                                onToggleFavorite = null,
                                modifier = rowModifier
                            )
                        } else {
                            PantrySwipeRow(
                                onDelete = { onIntent(ProductsIntent.DeleteProduct(product.id)) },
                                onFavorite = {
                                    onIntent(ProductsIntent.ToggleFavorite(product.id, !product.isFavorite))
                                },
                                modifier = rowModifier
                            ) {
                                ProductRow(
                                    product = product,
                                    subtitle = subtitle,
                                    dotColor = dotColor,
                                    favoriteColor = favoriteColor,
                                    selected = null,
                                    onClick = { editingProduct = product },
                                    onLongClick = { actionProduct = product },
                                    onToggleFavorite = {
                                        onIntent(ProductsIntent.ToggleFavorite(product.id, !product.isFavorite))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Replaces the header while selecting: close · "N selected" · select all · delete. */
@Composable
private fun SelectionBar(
    selectedCount: Int,
    allSelected: Boolean,
    onClose: () -> Unit,
    onSelectAll: () -> Unit,
    onDelete: () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.lg, vertical = spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.sm)
    ) {
        PantryHeaderIconButton(
            icon = PantryIcons.Close,
            contentDescription = stringResource(R.string.cancel_action),
            onClick = onClose
        )
        Text(
            text = pluralStringResource(R.plurals.selected_count, selectedCount, selectedCount),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .weight(1f)
                .padding(start = spacing.xs)
        )
        Surface(
            onClick = onSelectAll,
            shape = RoundedCornerShape(PantryHubTheme.radius.iconButton),
            color = if (allSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            },
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = PantryIcons.SelectAll,
                    contentDescription = stringResource(R.string.select_all_action),
                    tint = if (allSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        PantryHeaderIconButton(
            icon = PantryIcons.Delete,
            contentDescription = stringResource(R.string.delete_action),
            onClick = onDelete
        )
    }
}

/** Header filter icon button; tinted with the accent while a filter or sort is active. */
@Composable
private fun FilterButton(active: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(PantryHubTheme.radius.iconButton),
        color = if (active) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainer
        },
        modifier = Modifier.size(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = PantryIcons.Filter,
                contentDescription = stringResource(R.string.filter_title),
                tint = if (active) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun GroupLabel(
    header: ProductGroupHeader,
    count: Int,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null
) {
    when (header) {
        ProductGroupHeader.Favorites -> PantrySectionLabel(
            text = stringResource(R.string.favorites_section),
            color = PantryHubTheme.extendedColors.favorite,
            icon = PantryIcons.Favorite,
            count = count,
            action = action,
            modifier = modifier
        )
        is ProductGroupHeader.ByCategory -> PantrySectionLabel(
            text = header.category.name,
            dotColor = header.color,
            count = count,
            action = action,
            modifier = modifier
        )
        ProductGroupHeader.Uncategorized -> PantrySectionLabel(
            text = stringResource(R.string.product_no_category),
            dotColor = uncategorizedDotColor(),
            count = count,
            action = action,
            modifier = modifier
        )
        ProductGroupHeader.None -> Unit
    }
}

/**
 * One product row. With [selected] non-null the row is in selection mode: a checkbox
 * replaces the favorite star and the card is highlighted when ticked.
 */
@Composable
private fun ProductRow(
    product: Product,
    subtitle: String?,
    dotColor: Color,
    favoriteColor: Color,
    selected: Boolean?,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onToggleFavorite: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val spacing = PantryHubTheme.spacing
    PantryItemCard(
        modifier = modifier,
        onClick = onClick,
        onLongClick = onLongClick,
        containerColor = if (selected == true) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        }
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(dotColor, CircleShape)
        )
        Spacer(modifier = Modifier.width(spacing.md))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (selected == true) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (selected == true) {
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (selected != null) {
            PantryCheckbox(checked = selected, onCheckedChange = { onClick() })
        } else if (onToggleFavorite != null) {
            IconButton(onClick = onToggleFavorite, modifier = Modifier.size(40.dp)) {
                Icon(
                    imageVector = if (product.isFavorite) PantryIcons.Favorite else PantryIcons.FavoriteBorder,
                    contentDescription = stringResource(
                        if (product.isFavorite) R.string.unfavorite_action else R.string.favorite_action
                    ),
                    tint = if (product.isFavorite) favoriteColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/** Long-press menu for one product. */
@Composable
private fun ProductActionsSheet(
    product: Product,
    onEdit: () -> Unit,
    onToggleFavorite: () -> Unit,
    onSelectSeveral: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    PantrySheet(
        onDismissRequest = onDismiss,
        title = product.name,
        subtitle = stringResource(R.string.product_actions_subtitle)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            ActionRow(
                icon = PantryIcons.Edit,
                label = stringResource(R.string.product_action_edit),
                onClick = onEdit
            )
            ActionRow(
                icon = if (product.isFavorite) PantryIcons.FavoriteBorder else PantryIcons.Favorite,
                label = stringResource(
                    if (product.isFavorite) R.string.unfavorite_action else R.string.favorite_action
                ),
                onClick = onToggleFavorite
            )
            ActionRow(
                icon = PantryIcons.SelectAll,
                label = stringResource(R.string.product_action_select),
                onClick = onSelectSeveral
            )
            ActionRow(
                icon = PantryIcons.Delete,
                label = stringResource(R.string.product_action_delete),
                onClick = onDelete,
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun ActionRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(PantryHubTheme.radius.tile),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .height(52.dp)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = tint
            )
        }
    }
}
