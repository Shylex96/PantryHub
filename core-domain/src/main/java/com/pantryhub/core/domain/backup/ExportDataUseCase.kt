package com.pantryhub.core.domain.backup

import com.pantryhub.core.data.repository.CategoryRepository
import com.pantryhub.core.data.repository.NoteRepository
import com.pantryhub.core.data.repository.ProductRepository
import com.pantryhub.core.data.repository.PurchaseRepository
import com.pantryhub.core.data.repository.ShoppingListRepository
import com.pantryhub.core.model.backup.BackupData
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import javax.inject.Inject

class ExportDataUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val purchaseRepository: PurchaseRepository,
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(): BackupData {
        val products = productRepository.getProducts().first()
        val categories = categoryRepository.getCategories().first()
        val shoppingLists = shoppingListRepository.getLists().first()
        val purchases = purchaseRepository.getPurchases().first()
        val notes = noteRepository.getNotes().first()

        return BackupData(
            products = products,
            categories = categories,
            shoppingLists = shoppingLists,
            purchases = purchases,
            notes = notes,
            exportedAt = Clock.System.now().toEpochMilliseconds()
        )
    }
}
