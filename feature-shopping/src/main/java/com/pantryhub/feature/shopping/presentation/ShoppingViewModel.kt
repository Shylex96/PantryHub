package com.pantryhub.feature.shopping.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantryhub.core.model.product.Product
import com.pantryhub.core.model.shopping.ShoppingList
import com.pantryhub.core.model.shopping.ShoppingListItem
import com.pantryhub.core.model.shopping.ShoppingListType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingUiState())
    val uiState: StateFlow<ShoppingUiState> = _uiState.asStateFlow()

    init {
        loadMockData()
    }

    fun handleIntent(intent: ShoppingIntent) {
        when (intent) {
            ShoppingIntent.LoadLists -> loadMockData()
            is ShoppingIntent.CreateList -> createList(intent.name)
            is ShoppingIntent.OpenList -> openList(intent.id)
            is ShoppingIntent.AddItem -> addItem(intent.name, intent.quantity)
            is ShoppingIntent.DeleteItem -> deleteItem(intent.itemId)
            is ShoppingIntent.ToggleItem -> toggleItem(intent.itemId)
            ShoppingIntent.FinishShopping -> finishShopping()
        }
    }

    private fun loadMockData() {
        val now = Clock.System.now()
        val mockLists = listOf(
            ShoppingList(
                id = "1",
                name = "Weekly Groceries",
                type = ShoppingListType.REGULAR,
                createdAt = now,
                items = listOf(
                    ShoppingListItem(
                        id = "i1",
                        shoppingListId = "1",
                        product = Product("p1", "Milk", "milk", createdAt = now),
                        quantity = 2.0,
                        addedAt = now
                    ),
                    ShoppingListItem(
                        id = "i2",
                        shoppingListId = "1",
                        product = Product("p2", "Bread", "bread", createdAt = now),
                        quantity = 1.0,
                        addedAt = now,
                        isCompleted = true
                    )
                )
            ),
            ShoppingList(
                id = "2",
                name = "BBQ Party",
                type = ShoppingListType.TEMPORARY,
                createdAt = now
            )
        )
        _uiState.update { it.copy(lists = mockLists) }
    }

    private fun createList(name: String) {
        val newList = ShoppingList(
            id = UUID.randomUUID().toString(),
            name = name,
            type = ShoppingListType.REGULAR,
            createdAt = Clock.System.now()
        )
        _uiState.update { it.copy(lists = it.lists + newList) }
    }

    private fun openList(id: String) {
        val list = _uiState.value.lists.find { it.id == id }
        _uiState.update { it.copy(currentList = list) }
    }

    private fun addItem(name: String, quantity: Double) {
        _uiState.update { state ->
            val updatedList = state.currentList?.let { list ->
                val newItem = ShoppingListItem(
                    id = UUID.randomUUID().toString(),
                    shoppingListId = list.id,
                    product = Product(
                        id = UUID.randomUUID().toString(),
                        name = name,
                        normalizedName = name.lowercase(),
                        createdAt = Clock.System.now()
                    ),
                    quantity = quantity,
                    addedAt = Clock.System.now()
                )
                list.copy(items = list.items + newItem)
            }
            state.copy(
                currentList = updatedList,
                lists = state.lists.map { if (it.id == updatedList?.id) updatedList else it }
            )
        }
    }

    private fun deleteItem(itemId: String) {
        _uiState.update { state ->
            val updatedList = state.currentList?.let { list ->
                list.copy(items = list.items.filter { it.id != itemId })
            }
            state.copy(
                currentList = updatedList,
                lists = state.lists.map { if (it.id == updatedList?.id) updatedList else it }
            )
        }
    }

    private fun toggleItem(itemId: String) {
        _uiState.update { state ->
            val updatedList = state.currentList?.let { list ->
                list.copy(
                    items = list.items.map { item ->
                        if (item.id == itemId) {
                            val isCompleted = !item.isCompleted
                            item.copy(
                                isCompleted = isCompleted,
                                completedAt = if (isCompleted) Clock.System.now() else null
                            )
                        } else item
                    }
                )
            }
            state.copy(
                currentList = updatedList,
                lists = state.lists.map { if (it.id == updatedList?.id) updatedList else it }
            )
        }
    }

    private fun finishShopping() {
        // Logic to finalize will go here later
        _uiState.update { it.copy(currentList = null) }
    }
}
