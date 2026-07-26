package com.pantryhub.core.domain.product

import com.pantryhub.core.data.repository.ProductRepository
import com.pantryhub.core.model.product.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(query: String): Flow<List<Product>> {
        return productRepository.searchProducts(query)
    }
}
