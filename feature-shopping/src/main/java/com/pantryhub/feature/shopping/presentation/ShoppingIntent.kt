package com.pantryhub.feature.shopping.presentation

import com.pantryhub.core.model.shopping.ShoppingListType

sealed interface ShoppingIntent {
    data object LoadLists : ShoppingIntent
    data class CreateList(val name: String, val type: ShoppingListType) : ShoppingIntent
    data class CloneList(
        val sourceId: String,
        val name: String,
        val type: ShoppingListType
    ) : ShoppingIntent
    data class OpenList(val id: String) : ShoppingIntent
    data class DeleteList(val id: String) : ShoppingIntent
    data class RenameList(val newName: String) : ShoppingIntent
    data class UpdateProductQuery(val query: String) : ShoppingIntent
    data class AddItem(val name: String, val quantity: Double) : ShoppingIntent
    data class DeleteItem(val itemId: String) : ShoppingIntent
    data class ToggleItem(val itemId: String) : ShoppingIntent
    data class ToggleFavorite(val productId: String, val isFavorite: Boolean) : ShoppingIntent
    data class FinishWithData(val supermarket: String?, val price: Double?) : ShoppingIntent
    data object AcknowledgeFinished : ShoppingIntent
}
