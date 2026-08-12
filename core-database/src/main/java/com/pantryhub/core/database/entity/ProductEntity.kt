package com.pantryhub.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    @ColumnInfo(name = "normalized_name") val normalizedName: String,
    @ColumnInfo(name = "category_id") val categoryId: String?,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,
    @ColumnInfo(name = "usage_frequency") val usageFrequency: Int,
    @ColumnInfo(name = "created_at") val createdAt: Instant,
    // Display aliases joined by newline; normalized aliases (space-joined) for search.
    @ColumnInfo(name = "aliases", defaultValue = "''") val aliases: String = "",
    @ColumnInfo(name = "normalized_aliases", defaultValue = "''") val normalizedAliases: String = ""
)
