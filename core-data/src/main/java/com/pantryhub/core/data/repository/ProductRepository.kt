package com.pantryhub.core.data.repository

import com.pantryhub.core.model.product.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    suspend fun getProduct(id: String): Product?
    fun searchProducts(query: String): Flow<List<Product>>
    fun getProductsByCategory(categoryId: String): Flow<List<Product>>
    suspend fun saveProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    suspend fun incrementUsage(productId: String)
    suspend fun toggleFavorite(productId: String, isFavorite: Boolean)
    suspend fun clearCategoryFromProducts(categoryId: String)
}
