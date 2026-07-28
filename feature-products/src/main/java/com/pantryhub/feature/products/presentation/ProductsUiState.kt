package com.pantryhub.feature.products.presentation

import com.pantryhub.core.model.product.Product

data class ProductsUiState(
    val products: List<Product> = emptyList(),
    val searchQuery: String = "",
    val createInput: String = "",
    val isSearchMode: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
