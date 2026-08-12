package com.pantryhub.feature.products.presentation

import com.pantryhub.core.model.category.Category
import com.pantryhub.core.model.product.Product

data class ProductsUiState(
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val searchQuery: String = "",
    val createInput: String = "",
    val isSearchMode: Boolean = false,
    // Filter: null = all categories.
    val selectedCategoryId: String? = null,
    // Category assigned to the next product created (null = none).
    val newProductCategoryId: String? = null,
    val isManagingCategories: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
