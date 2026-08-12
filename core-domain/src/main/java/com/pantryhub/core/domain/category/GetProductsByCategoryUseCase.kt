package com.pantryhub.core.domain.category

import com.pantryhub.core.data.repository.ProductRepository
import com.pantryhub.core.model.product.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsByCategoryUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(categoryId: String): Flow<List<Product>> =
        repository.getProductsByCategory(categoryId)
}
