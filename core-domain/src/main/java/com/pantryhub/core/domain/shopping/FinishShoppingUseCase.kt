package com.pantryhub.core.domain.shopping

import com.pantryhub.core.data.repository.PurchaseRepository
import com.pantryhub.core.data.repository.ShoppingListRepository
import com.pantryhub.core.model.purchase.Purchase
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

class FinishShoppingUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val purchaseRepository: PurchaseRepository
) {
    /**
     * Completes a shopping session:
     * 1. Collects all completed items from the list.
     * 2. Creates a Purchase record.
     * 3. (Optional) Deletes or archives the original list.
     */
    suspend fun execute(listId: String, supermarket: String? = null, totalAmount: Double = 0.0) {
        val items = shoppingListRepository.getItemsForList(listId).first()
        val completedItems = items.filter { it.isCompleted }

        if (completedItems.isEmpty()) return

        val purchaseId = UUID.randomUUID().toString()
        val purchase = Purchase(
            id = purchaseId,
            date = Clock.System.now(),
            totalAmount = totalAmount,
            supermarket = supermarket,
            items = completedItems
        )

        purchaseRepository.savePurchase(purchase)

        // Data preservation: we no longer delete items from the list.
        // In the future, we might mark the list as "archived" or just clear the completion status.
    }
}
