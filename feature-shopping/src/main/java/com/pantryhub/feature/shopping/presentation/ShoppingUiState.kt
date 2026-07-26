package com.pantryhub.feature.shopping.presentation

import com.pantryhub.core.model.shopping.ShoppingList

data class ShoppingUiState(
    val lists: List<ShoppingList> = emptyList(),
    val currentList: ShoppingList? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
