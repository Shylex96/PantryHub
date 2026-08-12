package com.pantryhub.core.model.backup

import com.pantryhub.core.model.category.Category
import com.pantryhub.core.model.note.Note
import com.pantryhub.core.model.product.Product
import com.pantryhub.core.model.purchase.Purchase
import com.pantryhub.core.model.shopping.ShoppingList
import kotlinx.serialization.Serializable

@Serializable
data class BackupData(
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val shoppingLists: List<ShoppingList> = emptyList(),
    val purchases: List<Purchase> = emptyList(),
    val notes: List<Note> = emptyList(),
    val version: Int = 1,
    val exportedAt: Long // Epoch millis
)
