package com.pantryhub.feature.products.presentation

import com.pantryhub.core.model.category.Category
import com.pantryhub.core.model.product.Product

/** Which products the Products tab shows. */
sealed interface ProductFilter {
    data object All : ProductFilter
    data object Uncategorized : ProductFilter
    data class ByCategory(val categoryId: String) : ProductFilter
}

/** How the Products tab orders (and groups) products. */
enum class ProductSort {
    /** Grouped by category: Favorites first, then categories in order, "No category" last. */
    CATEGORY,
    /** Flat list, alphabetical. */
    NAME,
    /** Flat list, by how often a product has been added to lists. */
    MOST_USED
}

data class ProductsUiState(
    // Every product matching the current search query (before the category filter).
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val searchQuery: String = "",
    val filter: ProductFilter = ProductFilter.All,
    val sort: ProductSort = ProductSort.CATEGORY,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    /** True when the filter sheet changed anything from the defaults. */
    val isFilterActive: Boolean
        get() = filter != ProductFilter.All || sort != ProductSort.CATEGORY
}
