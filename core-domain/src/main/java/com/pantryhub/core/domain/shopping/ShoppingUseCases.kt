package com.pantryhub.core.domain.shopping

import com.pantryhub.core.data.repository.ProductRepository
import com.pantryhub.core.data.repository.ShoppingListRepository
import com.pantryhub.core.model.product.Product
import com.pantryhub.core.model.shopping.ShoppingList
import com.pantryhub.core.model.shopping.ShoppingListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Wrapper class to inject all shopping-related use cases with a single dependency.
 */
class ShoppingUseCases @Inject constructor(
    val createShoppingList: CreateShoppingListUseCase,
    val renameShoppingList: RenameShoppingListUseCase,
    val deleteShoppingList: DeleteShoppingListUseCase,
    val addProductToShoppingList: AddProductToShoppingListUseCase,
    val cloneShoppingList: CloneShoppingListUseCase,
    val finishShopping: FinishShoppingUseCase,
    val getShoppingLists: GetShoppingListsUseCase,
    val getShoppingListItems: GetShoppingListItemsUseCase,
    val toggleShoppingListItem: ToggleShoppingListItemUseCase,
    val deleteShoppingListItem: DeleteShoppingListItemUseCase
)

class GetShoppingListsUseCase @Inject constructor(
    private val repository: ShoppingListRepository
) {
    operator fun invoke(): Flow<List<ShoppingList>> = repository.getLists()
}

class GetShoppingListItemsUseCase @Inject constructor(
    private val repository: ShoppingListRepository
) {
    operator fun invoke(listId: String): Flow<List<ShoppingListItem>> = repository.getItemsForList(listId)
}

class ToggleShoppingListItemUseCase @Inject constructor(
    private val repository: ShoppingListRepository
) {
    suspend operator fun invoke(itemId: String, isCompleted: Boolean) {
        repository.updateItemCompletion(itemId, isCompleted)
    }
}

class DeleteShoppingListItemUseCase @Inject constructor(
    private val repository: ShoppingListRepository
) {
    suspend operator fun invoke(item: ShoppingListItem) {
        repository.deleteItem(item)
    }
}
