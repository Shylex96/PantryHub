package com.pantryhub.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(tableName = "purchases")
data class PurchaseEntity(
    @PrimaryKey val id: String,
    val date: Instant,
    @ColumnInfo(name = "total_amount") val totalAmount: Double,
    val supermarket: String?,
    @ColumnInfo(name = "created_at") val createdAt: Instant
)
