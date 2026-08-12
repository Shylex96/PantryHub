package com.pantryhub.feature.shopping.presentation

import com.pantryhub.core.model.category.Category
import com.pantryhub.core.model.product.Product
import com.pantryhub.core.model.shopping.ShoppingList

data class ShoppingUiState(
    val lists: List<ShoppingList> = emptyList(),
    val currentList: ShoppingList? = null,
    val productQuery: String = "",
    val suggestions: List<Product> = emptyList(),
    // All categories, used to resolve each product's color dot.
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    // One-shot flag: set true when a shopping session has finished so the UI can
    // navigate back to the lists screen. Reset via AcknowledgeFinished.
    val shoppingFinished: Boolean = false
)
