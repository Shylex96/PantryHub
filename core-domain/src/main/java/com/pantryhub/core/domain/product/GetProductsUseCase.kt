package com.pantryhub.core.domain.product

import com.pantryhub.core.data.repository.ProductRepository
import com.pantryhub.core.model.product.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> = repository.getProducts()
}
