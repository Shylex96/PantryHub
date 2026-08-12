package com.pantryhub.core.data.repository

import com.pantryhub.core.database.dao.ProductDao
import com.pantryhub.core.database.mapper.asDomainModel
import com.pantryhub.core.database.mapper.asEntity
import com.pantryhub.core.model.product.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineProductRepository @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {
    override fun getProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { entities ->
            entities.map { it.asDomainModel() }
        }
    }

    override suspend fun getProduct(id: String): Product? {
        return productDao.getProductById(id)?.asDomainModel()
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query).map { entities ->
            entities.map { it.asDomainModel() }
        }
    }

    override fun getProductsByCategory(categoryId: String): Flow<List<Product>> {
        return productDao.getProductsByCategory(categoryId).map { entities ->
            entities.map { it.asDomainModel() }
        }
    }

    override suspend fun saveProduct(product: Product) {
        productDao.insertProduct(product.asEntity())
    }

    override suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product.asEntity())
    }

    override suspend fun incrementUsage(productId: String) {
        productDao.incrementUsageFrequency(productId)
    }

    override suspend fun toggleFavorite(productId: String, isFavorite: Boolean) {
        productDao.updateFavoriteStatus(productId, isFavorite)
    }

    override suspend fun clearCategoryFromProducts(categoryId: String) {
        productDao.clearCategory(categoryId)
    }
}
