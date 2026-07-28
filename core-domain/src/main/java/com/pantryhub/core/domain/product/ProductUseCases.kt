package com.pantryhub.core.domain.product

import javax.inject.Inject

class ProductUseCases @Inject constructor(
    val getProducts: GetProductsUseCase,
    val saveProduct: SaveProductUseCase,
    val searchProducts: SearchProductsUseCase,
    val toggleFavoriteProduct: ToggleFavoriteProductUseCase,
    val deleteProduct: DeleteProductUseCase,
    val detectDuplicateProduct: DetectDuplicateProductUseCase
)
