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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.pantryhub.feature.products.presentation.ProductsIntent
import com.pantryhub.feature.products.presentation.ProductsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    state: ProductsUiState,
    onIntent: (ProductsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = PantryHubTheme.spacing
    val searchFocusRequester = remember { FocusRequester() }

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

                // Add Icon (10%)
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

                // Search/Cancel Icon (10%)
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
                                        // Swiping to the left deletes the item.
                                        onIntent(ProductsIntent.DeleteProduct(product.id))
                                        true
                                    }

                                    SwipeToDismissBoxValue.StartToEnd -> {
                                        // Swiping to the right marks the item as favorite.
                                        onIntent(
                                            ProductsIntent.ToggleFavorite(
                                                product.id,
                                                !product.isFavorite
                                            )
                                        )
                                        // I return false because I don't dismiss the item.
                                        // I only toggle its favorite state, and the swipe returns to its resting position.
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
                                        // I display a red background with the delete icon aligned to the right.
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    Color(0xFFB00020).copy(alpha = colorAlpha),
                                                    PantryHubTheme.shapes.medium
                                                )
                                                .padding(horizontal = spacing.xl),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            Icon(
                                                imageVector = PantryIcons.Delete,
                                                contentDescription = stringResource(R.string.delete_action),
                                                // tint = Color.White,
                                                modifier = Modifier.scale(scale)
                                            )
                                        }
                                    }

                                    SwipeToDismissBoxValue.StartToEnd -> {
                                        // I display a gold background with the favorite star aligned to the left.
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    Color(0xFFFFD700).copy(alpha = colorAlpha),
                                                    PantryHubTheme.shapes.medium
                                                )
                                                .padding(horizontal = spacing.xl),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            Icon(
                                                imageVector = PantryIcons.Favorite,
                                                contentDescription = null,
                                                tint = Color(0xFF7A5B00),
                                                modifier = Modifier.scale(scale)
                                            )
                                        }
                                    }

                                    SwipeToDismissBoxValue.Settled -> {
                                        // I don't show anything when there is no active swipe.
                                    }
                                }
                            }
                        ) {
                            PantryListItem(
                                title = product.name,
                                subtitle = null,
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
                                                // I previously used Color.Unspecified for the "not favorite" state.
                                                // On vectors without a base color, this could make the icon invisible.
                                                // I now use an explicit theme color instead.
                                                tint = if (product.isFavorite) {
                                                    Color(0xFFFFD700)
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