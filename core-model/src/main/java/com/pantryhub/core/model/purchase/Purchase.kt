package com.pantryhub.core.model.purchase

import com.pantryhub.core.model.shopping.ShoppingListItem
import kotlinx.datetime.Instant

data class Purchase(
    val id: String,
    val date: Instant,
    val totalAmount: Double,
    val supermarket: String? = null,
    val items: List<ShoppingListItem>
)
