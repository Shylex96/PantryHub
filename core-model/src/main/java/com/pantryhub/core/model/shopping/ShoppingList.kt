package com.pantryhub.core.model.shopping

import kotlinx.datetime.Instant

data class ShoppingList(
    val id: String,
    val name: String,
    val type: ShoppingListType,
    val items: List<ShoppingListItem> = emptyList(),
    val createdAt: Instant
)
