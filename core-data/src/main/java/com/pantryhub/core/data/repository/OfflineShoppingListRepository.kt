package com.pantryhub.core.data.repository

import com.pantryhub.core.database.dao.ProductDao
import com.pantryhub.core.database.dao.ShoppingListDao
import com.pantryhub.core.database.mapper.asDomainModel
import com.pantryhub.core.database.mapper.asEntity
import com.pantryhub.core.model.shopping.ShoppingList
import com.pantryhub.core.model.shopping.ShoppingListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject

class OfflineShoppingListRepository @Inject constructor(
    private val shoppingListDao: ShoppingListDao,
    private val productDao: ProductDao
) : ShoppingListRepository {
    override fun getLists(): Flow<List<ShoppingList>> {
        return shoppingListDao.getAllListsWithItems().map { wrappers ->
            wrappers.map { wrapper ->
                wrapper.list.asDomainModel(
                    items = wrapper.items.map { itemEntity ->
                        // Attach the real product so list items carry a valid product id
                        // (needed for a complete, re-importable export). Falls back to a
                        // placeholder that still preserves the product id.
                        val product = productDao.getProductById(itemEntity.productId)?.asDomainModel()
                            ?: dummyProduct().copy(id = itemEntity.productId)
                        itemEntity.asDomainModel(product)
                    }
                )
            }
        }
    }

    private fun dummyProduct() = com.pantryhub.core.model.product.Product(
        id = "",
        name = "",
        normalizedName = "",
        createdAt = Clock.System.now()
    )

    override suspend fun getList(id: String): ShoppingList? {
        return shoppingListDao.getListById(id)?.asDomainModel(items = emptyList())
    }

    override suspend fun saveList(list: ShoppingList) {
        shoppingListDao.updateList(list.asEntity())
        shoppingListDao.insertList(list.asEntity())
    }

    override suspend fun deleteList(list: ShoppingList) {
        shoppingListDao.deleteList(list.asEntity())
    }

    override fun getItemsForList(listId: String): Flow<List<ShoppingListItem>> {
        // This one-off fetch was outside Room's invalidation mechanism:
        // the Flow was only re-emitted when "shopping_items" changed, never when
        // "products" changed (e.g. when marking/unmarking a product as favorite).
        //
        // return shoppingListDao.getItemsForList(listId).map { entities ->
        //    entities.map { itemEntity ->
        //        val productEntity = productDao.getProductById(itemEntity.productId)
        //        val product = productEntity?.asDomainModel() ?: dummyProduct().copy(id = itemEntity.productId)
        //        itemEntity.asDomainModel(product)
        //    }
        // }

        // I now use the relational query (@Transaction + @Relation), which causes
        // Room to invalidate this Flow whenever either "shopping_items" or "products" changes.
        return shoppingListDao.getItemsWithProductForList(listId).map { rows ->
            rows.map { it.asDomainModel() }
        }
    }

    override suspend fun saveItem(item: ShoppingListItem) {
        shoppingListDao.insertItem(item.asEntity())
    }

    override suspend fun deleteItem(item: ShoppingListItem) {
        shoppingListDao.deleteItem(item.asEntity())
    }

    override suspend fun updateItemCompletion(itemId: String, isCompleted: Boolean) {
        val completedAt = if (isCompleted) Clock.System.now().toEpochMilliseconds() else null
        shoppingListDao.updateItemCompletion(itemId, isCompleted, completedAt)
    }

    override suspend fun resetList(listId: String) {
        shoppingListDao.resetListCompletion(listId)
    }
}
