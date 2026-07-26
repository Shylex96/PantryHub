package com.pantryhub.core.domain.shopping

import com.pantryhub.core.common.util.toComparisonKey
import com.pantryhub.core.common.util.toStorageName
import com.pantryhub.core.data.repository.ProductRepository
import com.pantryhub.core.data.repository.ShoppingListRepository
import com.pantryhub.core.domain.product.DetectDuplicateProductUseCase
import com.pantryhub.core.model.product.Product
import com.pantryhub.core.model.shopping.ShoppingListItem
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

class AddProductToShoppingListUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val detectDuplicateProductUseCase: DetectDuplicateProductUseCase
) {
    suspend operator fun invoke(listId: String, productName: String, quantity: Double = 1.0) {
        val storageName = productName.toStorageName()
        if (storageName.isEmpty()) return

        // 1. Detect or Create Product
        val existingProduct = detectDuplicateProductUseCase.execute(productName)
        val product = if (existingProduct != null) {
            existingProduct
        } else {
            val newProduct = Product(
                id = UUID.randomUUID().toString(),
                name = storageName,
                normalizedName = productName.toComparisonKey(),
                createdAt = Clock.System.now()
            )
            productRepository.saveProduct(newProduct)
            newProduct
        }

        // 2. Create and Save Shopping List Item
        val shoppingListItem = ShoppingListItem(
            id = UUID.randomUUID().toString(),
            shoppingListId = listId,
            product = product,
            quantity = quantity,
            addedAt = Clock.System.now()
        )
        
        shoppingListRepository.saveItem(shoppingListItem)
        
        // 3. Increment usage frequency
        productRepository.incrementUsage(product.id)
    }
}
