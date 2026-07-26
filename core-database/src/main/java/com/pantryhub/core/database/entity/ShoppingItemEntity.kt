package com.pantryhub.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "shopping_items",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingListEntity::class,
            parentColumns = ["id"],
            childColumns = ["shopping_list_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class ShoppingItemEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "shopping_list_id") val shoppingListId: String,
    @ColumnInfo(name = "product_id") val productId: String,
    val quantity: Double,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean,
    val price: Double?,
    @ColumnInfo(name = "added_at") val addedAt: Instant,
    @ColumnInfo(name = "completed_at") val completedAt: Instant?
)
