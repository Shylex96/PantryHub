package com.pantryhub.core.domain.product

import com.pantryhub.core.common.util.toComparisonKey
import com.pantryhub.core.data.repository.ProductRepository
import com.pantryhub.core.model.product.Product
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DetectDuplicateProductUseCase @Inject constructor(
    private val productRepository: ProductRepository,
) {
    /**
     * Checks if a product with the same normalized name already exists.
     * @return The existing product if found, null otherwise.
     */
    suspend fun execute(name: String): Product? {
        val comparisonKey = name.toComparisonKey()
        if (comparisonKey.isEmpty()) return null

        val existingProducts = productRepository.getProducts().first()
        return existingProducts.find { it.normalizedName == comparisonKey }
    }
}
