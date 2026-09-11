package com.pantryhub.feature.products.presentation

import com.pantryhub.core.model.category.Category

sealed interface ProductsIntent {
    data object LoadProducts : ProductsIntent
    data class Search(val query: String) : ProductsIntent
    data class ToggleFavorite(val productId: String, val isFavorite: Boolean) : ProductsIntent
    data class DeleteProduct(val productId: String) : ProductsIntent
    // Create a product from the "New product" sheet with an optional category.
    data class CreateProduct(val name: String, val categoryId: String?) : ProductsIntent
    // Update an existing product's category (null = remove) and its aliases.
    data class UpdateProductDetails(
        val productId: String,
        val categoryId: String?,
        val aliases: List<String>
    ) : ProductsIntent

    // Filter sheet (docs/04_UX_Guidelines.md, "Category Browsing").
    data class ApplyFilter(val filter: ProductFilter, val sort: ProductSort) : ProductsIntent
    data object ResetFilter : ProductsIntent

    // Categories
    data object OpenCategoryManager : ProductsIntent
    data object CloseCategoryManager : ProductsIntent
    data class CreateCategory(val name: String) : ProductsIntent
    data class RenameCategory(val id: String, val name: String) : ProductsIntent
    data class DeleteCategory(val category: Category) : ProductsIntent
}
