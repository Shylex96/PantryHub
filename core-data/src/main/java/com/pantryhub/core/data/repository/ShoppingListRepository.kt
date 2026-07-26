package com.pantryhub.core.data.repository

import com.pantryhub.core.model.shopping.ShoppingList
import com.pantryhub.core.model.shopping.ShoppingListItem
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun getLists(): Flow<List<ShoppingList>>
    suspend fun getList(id: String): ShoppingList?
    suspend fun saveList(list: ShoppingList)
    suspend fun deleteList(list: ShoppingList)
    fun getItemsForList(listId: String): Flow<List<ShoppingListItem>>
    suspend fun saveItem(item: ShoppingListItem)
    suspend fun deleteItem(item: ShoppingListItem)
    suspend fun updateItemCompletion(itemId: String, isCompleted: Boolean)
}
