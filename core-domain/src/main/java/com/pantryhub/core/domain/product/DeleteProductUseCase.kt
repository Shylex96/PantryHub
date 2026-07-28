package com.pantryhub.core.domain.product

import com.pantryhub.core.data.repository.ProductRepository
import com.pantryhub.core.model.product.Product
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        repository.deleteProduct(product)
    }
}
