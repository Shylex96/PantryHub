package com.pantryhub.core.domain.backup

import com.pantryhub.core.data.repository.ProductRepository
import com.pantryhub.core.data.repository.PurchaseRepository
import com.pantryhub.core.data.repository.ShoppingListRepository
import com.pantryhub.core.model.backup.BackupData
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import javax.inject.Inject

class ExportDataUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val purchaseRepository: PurchaseRepository
) {
    suspend operator fun invoke(): BackupData {
        val products = productRepository.getProducts().first()
        val shoppingLists = shoppingListRepository.getLists().first()
        val purchases = purchaseRepository.getPurchases().first()
        
        return BackupData(
            products = products,
            categories = emptyList(), // Future: add categories
            shoppingLists = shoppingLists,
            purchases = purchases,
            exportedAt = Clock.System.now().toEpochMilliseconds()
        )
    }
}
