package com.pantryhub.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pantryhub.core.model.shopping.ShoppingListType
import kotlinx.datetime.Instant

@Entity(tableName = "shopping_lists")
data class ShoppingListEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: ShoppingListType,
    @ColumnInfo(name = "created_at") val createdAt: Instant
)
