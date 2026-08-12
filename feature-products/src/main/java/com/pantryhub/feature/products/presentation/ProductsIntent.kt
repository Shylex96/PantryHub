package com.pantryhub.feature.products.presentation

import com.pantryhub.core.model.category.Category

sealed interface ProductsIntent {
    data object LoadProducts : ProductsIntent
    data class Search(val query: String) : ProductsIntent
    data class ToggleFavorite(val productId: String, val isFavorite: Boolean) : ProductsIntent
    data class DeleteProduct(val productId: String) : ProductsIntent
    data class UpdateCreateInput(val input: String) : ProductsIntent
    data class CreateProduct(val name: String) : ProductsIntent
    data object ToggleSearchMode : ProductsIntent
    // Update an existing product's category (null = remove) and its aliases.
    data class UpdateProductDetails(
        val productId: String,
        val categoryId: String?,
        val aliases: List<String>
    ) : ProductsIntent

    // Categories
    data class SelectCategoryFilter(val categoryId: String?) : ProductsIntent
    data class SetNewProductCategory(val categoryId: String?) : ProductsIntent
    data object OpenCategoryManager : ProductsIntent
    data object CloseCategoryManager : ProductsIntent
    data class CreateCategory(val name: String) : ProductsIntent
    data class RenameCategory(val id: String, val name: String) : ProductsIntent
    data class DeleteCategory(val category: Category) : ProductsIntent
}
