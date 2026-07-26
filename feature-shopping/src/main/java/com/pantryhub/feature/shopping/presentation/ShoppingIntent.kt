package com.pantryhub.feature.shopping.presentation

sealed interface ShoppingIntent {
    data object LoadLists : ShoppingIntent
    data class CreateList(val name: String) : ShoppingIntent
    data class OpenList(val id: String) : ShoppingIntent
    data class AddItem(val name: String, val quantity: Double) : ShoppingIntent
    data class DeleteItem(val itemId: String) : ShoppingIntent
    data class ToggleItem(val itemId: String) : ShoppingIntent
    data object FinishShopping : ShoppingIntent
}
