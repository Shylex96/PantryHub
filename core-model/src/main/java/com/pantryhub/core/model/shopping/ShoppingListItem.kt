package com.pantryhub.core.model.shopping

import com.pantryhub.core.model.product.Product
import kotlinx.datetime.Instant

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
