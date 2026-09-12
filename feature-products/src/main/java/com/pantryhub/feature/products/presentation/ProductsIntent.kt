package com.pantryhub.feature.products.presentation

sealed interface ProductsIntent {
    data object LoadProducts : ProductsIntent
    data class Search(val query: String) : ProductsIntent
    data class ToggleFavorite(val productId: String, val isFavorite: Boolean) : ProductsIntent
    data class DeleteProduct(val productId: String) : ProductsIntent
    // Create a product from the "New product" sheet with an optional category.
    data class CreateProduct(val name: String, val categoryId: String?) : ProductsIntent
    // Update an existing product: name, category (null = remove) and aliases.
    data class UpdateProductDetails(
        val productId: String,
        val name: String,
        val categoryId: String?,
        val aliases: List<String>
    ) : ProductsIntent

    // Multi-select actions (docs/04_UX_Guidelines.md "Category Browsing").
    data class AssignCategory(val productIds: Set<String>, val categoryId: String?) : ProductsIntent
    data class DeleteProducts(val productIds: Set<String>) : ProductsIntent

    // Filter sheet.
    data class ApplyFilter(val filter: ProductFilter, val sort: ProductSort) : ProductsIntent
    data object ResetFilter : ProductsIntent
}
