package com.pantryhub.feature.products.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryEmptyState
import com.pantryhub.core.designsystem.ui.components.PantryExtendedFab
import com.pantryhub.core.designsystem.ui.components.PantryItemCard
import com.pantryhub.core.designsystem.ui.components.PantryLoading
import com.pantryhub.core.designsystem.ui.components.PantryScreenHeader
import com.pantryhub.core.designsystem.ui.components.PantrySearchField
import com.pantryhub.core.designsystem.ui.components.PantrySectionLabel
import com.pantryhub.core.designsystem.ui.components.PantrySwipeRow
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.designsystem.ui.theme.uncategorizedDotColor
import com.pantryhub.core.model.product.Product
import com.pantryhub.feature.products.presentation.ProductFilter
import com.pantryhub.feature.products.presentation.ProductsIntent
import com.pantryhub.feature.products.presentation.ProductsUiState
import com.pantryhub.feature.products.ui.components.CategoryManagerSheet
import com.pantryhub.feature.products.ui.components.EditProductSheet
import com.pantryhub.feature.products.ui.components.NewProductSheet
import com.pantryhub.feature.products.ui.components.ProductFilterSheet
import com.pantryhub.feature.products.ui.components.ProductGroupHeader
import com.pantryhub.feature.products.ui.components.productDotColor
import com.pantryhub.feature.products.ui.components.rememberProductGroups

/**
 * Products tab (docs/04_UX_Guidelines.md "Category Browsing", docs/05 §6.1, §6.7):
 * large header with live counts and a filter icon button, permanent search pill,
 * products grouped by category (Favorites first, "No category" last), extended FAB to
 * create a product. Rows show only the favorite star; delete = swipe left; tap = edit.
 */
@Composable
fun ProductsScreen(
    state: ProductsUiState,
    onIntent: (ProductsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = PantryHubTheme.spacing
    val favoriteColor = PantryHubTheme.extendedColors.favorite

    var showFilterSheet by remember { mutableStateOf(false) }
    var showNewProductSheet by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<Product?>(null) }

    val groups = rememberProductGroups(
        products = state.products,
        categories = state.categories,
        filter = state.filter,
        sort = state.sort
    )
    val visibleCount = groups.sumOf { it.products.size }

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
                onIntent(ProductsIntent.OpenCategoryManager)
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
            },
            onDismiss = { showNewProductSheet = false }
        )
    }

    val editing = editingProduct
    if (editing != null) {
        EditProductSheet(
            product = editing,
            categories = state.categories,
            onSave = { categoryId, aliases ->
                onIntent(ProductsIntent.UpdateProductDetails(editing.id, categoryId, aliases))
                editingProduct = null
            },
            onDismiss = { editingProduct = null }
        )
    }

    if (state.isManagingCategories) {
        CategoryManagerSheet(
            categories = state.categories,
            onCreate = { onIntent(ProductsIntent.CreateCategory(it)) },
            onRename = { id, name -> onIntent(ProductsIntent.RenameCategory(id, name)) },
            onDelete = { onIntent(ProductsIntent.DeleteCategory(it)) },
            onDismiss = { onIntent(ProductsIntent.CloseCategoryManager) }
        )
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            PantryExtendedFab(
                text = stringResource(R.string.fab_new_product),
                icon = PantryIcons.Add,
                onClick = { showNewProductSheet = true }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(top = spacing.lg, bottom = 96.dp)
        ) {
            item(key = "header") {
                PantryScreenHeader(
                    title = stringResource(R.string.nav_products),
                    subtitle = pluralStringResource(
                        R.plurals.products_count, state.products.size, state.products.size
                    ) + " · " + pluralStringResource(
                        R.plurals.categories_count, state.categories.size, state.categories.size
                    ),
                    action = {
                        FilterButton(
                            active = state.isFilterActive,
                            onClick = { showFilterSheet = true }
                        )
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
                            GroupLabel(
                                header = header,
                                count = group.products.size,
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

                        PantrySwipeRow(
                            onDelete = { onIntent(ProductsIntent.DeleteProduct(product.id)) },
                            onFavorite = {
                                onIntent(ProductsIntent.ToggleFavorite(product.id, !product.isFavorite))
                            },
                            modifier = Modifier
                                .animateItem()
                                .padding(horizontal = spacing.screen, vertical = spacing.xs)
                        ) {
                            ProductRow(
                                product = product,
                                subtitle = subtitle,
                                dotColor = productDotColor(product.categoryId, state.categories),
                                favoriteColor = favoriteColor,
                                onClick = { editingProduct = product },
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
    modifier: Modifier = Modifier
) {
    when (header) {
        ProductGroupHeader.Favorites -> PantrySectionLabel(
            text = stringResource(R.string.favorites_section),
            color = PantryHubTheme.extendedColors.favorite,
            icon = PantryIcons.Favorite,
            count = count,
            modifier = modifier
        )
        is ProductGroupHeader.ByCategory -> PantrySectionLabel(
            text = header.category.name,
            dotColor = header.color,
            count = count,
            modifier = modifier
        )
        ProductGroupHeader.Uncategorized -> PantrySectionLabel(
            text = stringResource(R.string.product_no_category),
            dotColor = uncategorizedDotColor(),
            count = count,
            modifier = modifier
        )
        ProductGroupHeader.None -> Unit
    }
}

@Composable
private fun ProductRow(
    product: Product,
    subtitle: String?,
    dotColor: Color,
    favoriteColor: Color,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    PantryItemCard(onClick = onClick) {
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
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
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
