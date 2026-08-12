package com.pantryhub.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pantryhub.core.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: String): ProductEntity?

    @Query(
        "SELECT * FROM products WHERE normalized_name LIKE '%' || :query || '%' " +
            "OR normalized_aliases LIKE '%' || :query || '%'"
    )
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE category_id = :categoryId")
    fun getProductsByCategory(categoryId: String): Flow<List<ProductEntity>>

    @Query("UPDATE products SET category_id = NULL WHERE category_id = :categoryId")
    suspend fun clearCategory(categoryId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("UPDATE products SET usage_frequency = usage_frequency + 1 WHERE id = :productId")
    suspend fun incrementUsageFrequency(productId: String)

    @Query("UPDATE products SET is_favorite = :isFavorite WHERE id = :productId")
    suspend fun updateFavoriteStatus(productId: String, isFavorite: Boolean)
}
