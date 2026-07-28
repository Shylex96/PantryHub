package com.pantryhub.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Room POJO that combines a ShoppingItemEntity with its associated ProductEntity.
 *
 * When used inside a DAO method annotated with @Transaction that returns a Flow,
 * Room automatically observes invalidation for both the "shopping_items" and
 * "products" tables.
 *
 */
data class ShoppingItemWithProduct(
    @Embedded val item: ShoppingItemEntity,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "id"
    )
    val product: ProductEntity
)
