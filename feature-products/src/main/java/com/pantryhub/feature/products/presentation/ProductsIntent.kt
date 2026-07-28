package com.pantryhub.feature.products.presentation

sealed interface ProductsIntent {
    data object LoadProducts : ProductsIntent
    data class Search(val query: String) : ProductsIntent
    data class ToggleFavorite(val productId: String, val isFavorite: Boolean) : ProductsIntent
    data class DeleteProduct(val productId: String) : ProductsIntent
    data class UpdateCreateInput(val input: String) : ProductsIntent
    data class CreateProduct(val name: String) : ProductsIntent
    data object ToggleSearchMode : ProductsIntent
}
