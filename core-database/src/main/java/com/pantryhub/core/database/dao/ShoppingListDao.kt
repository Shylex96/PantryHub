package com.pantryhub.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pantryhub.core.database.entity.ShoppingItemEntity
import com.pantryhub.core.database.entity.ShoppingListEntity
import com.pantryhub.core.database.entity.ShoppingListWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Transaction
    @Query("SELECT * FROM shopping_lists ORDER BY created_at DESC")
    fun getAllListsWithItems(): Flow<List<ShoppingListWithItems>>

    @Query("SELECT * FROM shopping_lists WHERE id = :id")
    suspend fun getListById(id: String): ShoppingListEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(list: ShoppingListEntity)

    @Update
    suspend fun updateList(list: ShoppingListEntity)

    @Delete
    suspend fun deleteList(list: ShoppingListEntity)

    @Query("SELECT * FROM shopping_items WHERE shopping_list_id = :listId")
    fun getItemsForList(listId: String): Flow<List<ShoppingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingItemEntity)

    @Update
    suspend fun updateItem(item: ShoppingItemEntity)

    @Delete
    suspend fun deleteItem(item: ShoppingItemEntity)

    @Transaction
    @Query("UPDATE shopping_items SET is_completed = :isCompleted, completed_at = :completedAt WHERE id = :itemId")
    suspend fun updateItemCompletion(itemId: String, isCompleted: Boolean, completedAt: Long?)

    @Query("UPDATE shopping_items SET is_completed = 0, completed_at = NULL WHERE shopping_list_id = :listId")
    suspend fun resetListCompletion(listId: String)
}
