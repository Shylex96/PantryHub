package com.pantryhub.core.domain.product

import com.pantryhub.core.data.repository.ProductRepository
import javax.inject.Inject

class ToggleFavoriteProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: String, isFavorite: Boolean) {
        repository.toggleFavorite(productId, isFavorite)
    }
}
