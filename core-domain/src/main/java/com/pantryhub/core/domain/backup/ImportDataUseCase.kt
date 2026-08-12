package com.pantryhub.core.domain.backup

import com.pantryhub.core.data.repository.ProductRepository
import com.pantryhub.core.data.repository.PurchaseRepository
import com.pantryhub.core.data.repository.ShoppingListRepository
import com.pantryhub.core.model.backup.BackupData
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ImportDataUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val purchaseRepository: PurchaseRepository
) {
    suspend operator fun invoke(data: BackupData) {
        // 1. Import Products (Merge by name)
        val existingProducts = productRepository.getProducts().first()
        data.products.forEach { imported ->
            val match = existingProducts.find { it.normalizedName == imported.normalizedName }
            if (match == null) {
                productRepository.saveProduct(imported)
            }
        }

        // 2. Import Shopping Lists
        data.shoppingLists.forEach { list ->
            shoppingListRepository.saveList(list)
            list.items.forEach { item ->
                shoppingListRepository.saveItem(item)
            }
        }

        // 3. Import Purchases
        data.purchases.forEach { purchase ->
            purchaseRepository.savePurchase(purchase)
        }
    }
}
