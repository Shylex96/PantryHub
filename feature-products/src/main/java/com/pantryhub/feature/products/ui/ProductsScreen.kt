package com.pantryhub.feature.products.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryEmptyState
import com.pantryhub.core.designsystem.ui.components.PantryListItem
import com.pantryhub.core.designsystem.ui.components.PantryLoading
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.components.PantryTopBar
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.model.product.Product
import com.pantryhub.feature.products.presentation.ProductsIntent
import com.pantryhub.feature.products.presentation.ProductsUiState
import com.pantryhub.feature.products.ui.components.CategoryFilterRow
import com.pantryhub.feature.products.ui.components.CategoryManagerDialog
import com.pantryhub.feature.products.ui.components.EditProductDialog

@OptIn(ExperimentalMaterial3Api::class)
// SwipeToDismiss confirmValueChange is deprecated without a drop-in replacement;
// migration to dynamic anchors is tracked for the polish sprint.
@Suppress("DEPRECATION")
@Composable
fun ProductsScreen(
    state: ProductsUiState,
    onIntent: (ProductsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = PantryHubTheme.spacing
    val searchFocusRequester = remember { FocusRequester() }
    val favoriteColor = PantryHubTheme.extendedColors.favorite
    val onFavoriteColor = PantryHubTheme.extendedColors.onFavorite
    val deleteColor = MaterialTheme.colorScheme.error

    val extended = PantryHubTheme.extendedColors
    val categoryPalette = listOf(
        extended.categoryVegetables,
        extended.categoryFruit,
        extended.categoryDairy,
        extended.categoryMeat,
        extended.categoryBakery,
        extended.categoryDrinks,
        extended.categoryFrozen,
        extended.categoryHousehold,
        extended.categoryOther
    )
    // Deterministic color per category, based on its position in the list.
    val colorForCategory: (String) -> Color = { id ->
        val index = state.categories.indexOfFirst { it.id == id }
        if (index >= 0) categoryPalette[index % categoryPalette.size] else extended.categoryOther
    }

    var categorizingProduct by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(state.isSearchMode) {
        if (state.isSearchMode) {
            searchFocusRequester.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            PantryTopBar(title = stringResource(R.string.nav_products))
        }
    ) { innerPadding ->
        if (state.isManagingCategories) {
            CategoryManagerDialog(
                categories = state.categories,
                categoryColor = colorForCategory,
                onCreate = { onIntent(ProductsIntent.CreateCategory(it)) },
                onRename = { id, name -> onIntent(ProductsIntent.RenameCategory(id, name)) },
                onDelete = { onIntent(ProductsIntent.DeleteCategory(it)) },
                onDismiss = { onIntent(ProductsIntent.CloseCategoryManager) }
            )
        }

        val categorizing = categorizingProduct
        if (categorizing != null) {
            EditProductDialog(
                productName = categorizing.name,
                categories = state.categories,
                initialCategoryId = categorizing.categoryId,
                initialAliases = categorizing.aliases,
                categoryColor = colorForCategory,
                onSave = { categoryId, aliases ->
                    onIntent(ProductsIntent.UpdateProductDetails(categorizing.id, categoryId, aliases))
                    categorizingProduct = null
                },
                onDismiss = { categorizingProduct = null }
            )
        }

        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = spacing.lg)
        ) {

            // Symmetrical Dual Bar (Add / Search)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacing.md),
                horizontalArrangement = Arrangement.spacedBy(spacing.sm)
            ) {
                Box(modifier = Modifier.weight(0.8f)) {
                    AnimatedContent(
                        targetState = state.isSearchMode,
                        label = "bar_input"
                    ) { isSearching ->
                        if (isSearching) {
                            PantryTextField(
                                value = state.searchQuery,
                                onValueChange = { onIntent(ProductsIntent.Search(it)) },
                                label = stringResource(R.string.search_products_placeholder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(searchFocusRequester)
                            )
                        } else {
                            PantryTextField(
                                value = state.createInput,
                                onValueChange = { onIntent(ProductsIntent.UpdateCreateInput(it)) },
                                label = stringResource(R.string.add_item_label),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Add Icon
                IconButton(
                    onClick = {
                        if (state.isSearchMode) {
                            onIntent(ProductsIntent.ToggleSearchMode)
                        } else {
                            onIntent(ProductsIntent.CreateProduct(state.createInput))
                        }
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = PantryIcons.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Search/Cancel Icon
                IconButton(
                    onClick = { onIntent(ProductsIntent.ToggleSearchMode) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (state.isSearchMode) PantryIcons.Close else PantryIcons.Search,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Category selector for the next created product (create mode only)
            if (!state.isSearchMode) {
                var categoryMenuExpanded by remember { mutableStateOf(false) }
                val selectedNewCategory = state.categories.find { it.id == state.newProductCategoryId }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = spacing.sm),
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm)
                ) {
                    Text(
                        text = stringResource(R.string.new_product_category_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Box {
                        AssistChip(
                            onClick = { categoryMenuExpanded = true },
                            label = {
                                Text(selectedNewCategory?.name ?: stringResource(R.string.category_none))
                            },
                            leadingIcon = if (selectedNewCategory != null) {
                                {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(colorForCategory(selectedNewCategory.id), CircleShape)
                                    )
                                }
                            } else {
                                null
                            }
                        )
                        DropdownMenu(
                            expanded = categoryMenuExpanded,
                            onDismissRequest = { categoryMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.category_none)) },
                                onClick = {
                                    onIntent(ProductsIntent.SetNewProductCategory(null))
                                    categoryMenuExpanded = false
                                }
                            )
                            state.categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        onIntent(ProductsIntent.SetNewProductCategory(category.id))
                                        categoryMenuExpanded = false
                                    },
                                    leadingIcon = {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .background(colorForCategory(category.id), CircleShape)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Category filter chips
            CategoryFilterRow(
                categories = state.categories,
                selectedCategoryId = state.selectedCategoryId,
                categoryColor = colorForCategory,
                onSelect = { onIntent(ProductsIntent.SelectCategoryFilter(it)) },
                onManage = { onIntent(ProductsIntent.OpenCategoryManager) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = spacing.sm)
            )

            if (state.isLoading) {
                PantryLoading()
            } else if (state.products.isEmpty()) {
                val isSearching = state.searchQuery.isNotEmpty()
                PantryEmptyState(
                    title = if (isSearching) stringResource(R.string.search_no_results_title) else stringResource(
                        R.string.empty_products_title
                    ),
                    description = if (isSearching) stringResource(
                        R.string.search_no_results_desc,
                        state.searchQuery
                    ) else stringResource(R.string.empty_products_desc),
                    icon = if (isSearching) PantryIcons.Search else PantryIcons.Products
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.products, key = { it.id }) { product ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = {
                                when (it) {
                                    SwipeToDismissBoxValue.EndToStart -> {
                                        onIntent(ProductsIntent.DeleteProduct(product.id))
                                        true
                                    }

                                    SwipeToDismissBoxValue.StartToEnd -> {
                                        onIntent(
                                            ProductsIntent.ToggleFavorite(
                                                product.id,
                                                !product.isFavorite
                                            )
                                        )
                                        false
                                    }

                                    else -> false
                                }
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = true,
                            enableDismissFromEndToStart = true,
                            backgroundContent = {
                                val progress = dismissState.progress
                                val colorAlpha = (progress * 2f).coerceAtMost(1f)
                                val iconProgress = ((progress - 0.20f) / 0.80f).coerceAtLeast(0f)
                                val scale = 0.5f + (iconProgress * 0.5f).coerceAtMost(0.5f)

                                when (dismissState.dismissDirection) {
                                    SwipeToDismissBoxValue.EndToStart -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    deleteColor.copy(alpha = colorAlpha),
                                                    PantryHubTheme.shapes.medium
                                                )
                                                .padding(horizontal = spacing.xl),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            Icon(
                                                imageVector = PantryIcons.Delete,
                                                contentDescription = stringResource(R.string.delete_action),
                                                modifier = Modifier.scale(scale)
                                            )
                                        }
                                    }

                                    SwipeToDismissBoxValue.StartToEnd -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    favoriteColor.copy(alpha = colorAlpha),
                                                    PantryHubTheme.shapes.medium
                                                )
                                                .padding(horizontal = spacing.xl),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            Icon(
                                                imageVector = PantryIcons.Favorite,
                                                contentDescription = null,
                                                tint = onFavoriteColor,
                                                modifier = Modifier.scale(scale)
                                            )
                                        }
                                    }

                                    SwipeToDismissBoxValue.Settled -> {
                                    }
                                }
                            }
                        ) {
                            val productCategoryId = product.categoryId
                            PantryListItem(
                                title = product.name,
                                subtitle = null,
                                onClick = { categorizingProduct = product },
                                leadingContent = if (productCategoryId != null) {
                                    {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .background(colorForCategory(productCategoryId), CircleShape)
                                        )
                                    }
                                } else {
                                    null
                                },
                                trailingContent = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = {
                                                onIntent(
                                                    ProductsIntent.ToggleFavorite(
                                                        product.id,
                                                        !product.isFavorite
                                                    )
                                                )
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (product.isFavorite) {
                                                    PantryIcons.Favorite
                                                } else {
                                                    PantryIcons.FavoriteBorder
                                                },
                                                contentDescription = null,
                                                tint = if (product.isFavorite) {
                                                    favoriteColor
                                                } else {
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                                },
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(spacing.sm))
                                        IconButton(
                                            onClick = {
                                                onIntent(
                                                    ProductsIntent.DeleteProduct(
                                                        product.id
                                                    )
                                                )
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = PantryIcons.Delete,
                                                contentDescription = stringResource(R.string.delete_action),
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
