package com.pantryhub.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "purchase_items",
    foreignKeys = [
        ForeignKey(
            entity = PurchaseEntity::class,
            parentColumns = ["id"],
            childColumns = ["purchase_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PurchaseItemEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "purchase_id") val purchaseId: String,
    @ColumnInfo(name = "product_id") val productId: String,
    val quantity: Double,
    val price: Double?
)
