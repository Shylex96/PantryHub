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
                    items = wrapper.items.map { 
                        it.asDomainModel(dummyProduct()) 
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
        return shoppingListDao.getItemsForList(listId).map { entities ->
            entities.map { itemEntity ->
                val productEntity = productDao.getProductById(itemEntity.productId)
                val product = productEntity?.asDomainModel() ?: dummyProduct().copy(id = itemEntity.productId)
                itemEntity.asDomainModel(product) 
            }
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
