package com.pantryhub.core.model.purchase

import kotlinx.serialization.Serializable
import com.pantryhub.core.model.shopping.ShoppingListItem
import kotlinx.datetime.Instant

@Serializable
data class Purchase(
    val id: String,
    val date: Instant,
    val totalAmount: Double,
    val supermarket: String? = null,
    val items: List<ShoppingListItem>
)
