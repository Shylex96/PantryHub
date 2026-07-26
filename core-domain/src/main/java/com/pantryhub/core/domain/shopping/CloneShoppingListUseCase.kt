package com.pantryhub.core.domain.shopping

import com.pantryhub.core.data.repository.ShoppingListRepository
import com.pantryhub.core.model.shopping.ShoppingList
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

class CloneShoppingListUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) {
    /**
     * Creates a copy of an existing shopping list including all its items.
     * All items are reset to non-completed status in the new list.
     */
    suspend fun execute(originalListId: String, newName: String? = null) {
        val originalList = shoppingListRepository.getList(originalListId) ?: return
        val originalItems = shoppingListRepository.getItemsForList(originalListId).first()

        val newListId = UUID.randomUUID().toString()
        val now = Clock.System.now()

        val clonedList = ShoppingList(
            id = newListId,
            name = newName ?: "${originalList.name} (Copy)",
            type = originalList.type,
            createdAt = now
        )

        shoppingListRepository.saveList(clonedList)

        originalItems.forEach { item ->
            val clonedItem = item.copy(
                id = UUID.randomUUID().toString(),
                shoppingListId = newListId,
                isCompleted = false,
                addedAt = now,
                completedAt = null
            )
            shoppingListRepository.saveItem(clonedItem)
        }
    }
}
