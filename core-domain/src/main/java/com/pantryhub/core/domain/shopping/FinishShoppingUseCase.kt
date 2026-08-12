package com.pantryhub.core.domain.shopping

import com.pantryhub.core.data.repository.PurchaseRepository
import com.pantryhub.core.data.repository.ShoppingListRepository
import com.pantryhub.core.model.purchase.Purchase
import com.pantryhub.core.model.shopping.ShoppingListType
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
     * 1. Records a Purchase for whatever was actually bought (completed items).
     * 2. Disposes of the list according to its type:
     *    - One-off (TEMPORARY) lists are meant to be used once, so they are
     *      deleted when the session ends.
     *    - Regular lists are reset (all items back to pending) so they can be
     *      reused for the next shop.
     */
    suspend fun execute(listId: String, supermarket: String? = null, totalAmount: Double = 0.0) {
        val list = shoppingListRepository.getList(listId)
        val items = shoppingListRepository.getItemsForList(listId).first()
        val completedItems = items.filter { it.isCompleted }

        // Only record a purchase when something was checked off.
        if (completedItems.isNotEmpty()) {
            val purchaseId = UUID.randomUUID().toString()
            val purchase = Purchase(
                id = purchaseId,
                date = Clock.System.now(),
                totalAmount = totalAmount,
                supermarket = supermarket,
                items = completedItems
            )
            purchaseRepository.savePurchase(purchase)
        }

        // Dispose of the list based on its type.
        if (list != null && list.type == ShoppingListType.TEMPORARY) {
            shoppingListRepository.deleteList(list)
        } else {
            // Reset list state: set all items to pending.
            shoppingListRepository.resetList(listId)
        }
    }
}
