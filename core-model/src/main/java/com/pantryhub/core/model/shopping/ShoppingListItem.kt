package com.pantryhub.core.model.shopping

import kotlinx.serialization.Serializable
import com.pantryhub.core.model.product.Product
import kotlinx.datetime.Instant

@Serializable
data class ShoppingListItem(
    val id: String,
    val shoppingListId: String,
    val product: Product,
    val quantity: Double,
    val isCompleted: Boolean = false,
    val price: Double? = null,
    val addedAt: Instant,
    val completedAt: Instant? = null
)
