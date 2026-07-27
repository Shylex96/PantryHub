package com.pantryhub.feature.shopping.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantryhub.core.domain.shopping.ShoppingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val shoppingUseCases: ShoppingUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingUiState())
    val uiState: StateFlow<ShoppingUiState> = _uiState.asStateFlow()

    private var itemsObservationJob: Job? = null

    init {
        observeLists()
    }

    fun handleIntent(intent: ShoppingIntent) {
        when (intent) {
            ShoppingIntent.LoadLists -> observeLists()
            is ShoppingIntent.CreateList -> createList(intent.name)
            is ShoppingIntent.OpenList -> openList(intent.id)
            is ShoppingIntent.DeleteList -> deleteList(intent.id)
            is ShoppingIntent.RenameList -> renameList(intent.newName)
            is ShoppingIntent.AddItem -> addItem(intent.name, intent.quantity)
            is ShoppingIntent.DeleteItem -> deleteItem(intent.itemId)
            is ShoppingIntent.ToggleItem -> toggleItem(intent.itemId)
            is ShoppingIntent.FinishWithData -> finishShopping(intent.supermarket, intent.price)
        }
    }

    private fun observeLists() {
        shoppingUseCases.getShoppingLists()
            .onEach { lists ->
                _uiState.update { it.copy(lists = lists) }
            }
            .launchIn(viewModelScope)
    }

    private fun createList(name: String) {
        viewModelScope.launch {
            shoppingUseCases.createShoppingList(name)
        }
    }

    private fun openList(id: String) {
        itemsObservationJob?.cancel()
        
        _uiState.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            // Fetch list metadata immediately for faster UI feedback
            val list = shoppingUseCases.getShoppingList(id)
            _uiState.update { it.copy(currentList = list) }
            
            // Start observing items for this specific list reactively
            itemsObservationJob = shoppingUseCases.getShoppingListItems(id)
                .onEach { items ->
                    _uiState.update { state ->
                        state.copy(
                            currentList = state.currentList?.copy(items = items),
                            isLoading = false
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun deleteList(id: String) {
        viewModelScope.launch {
            shoppingUseCases.deleteShoppingList(id)
            if (_uiState.value.currentList?.id == id) {
                _uiState.update { it.copy(currentList = null) }
            }
        }
    }

    private fun renameList(newName: String) {
        val currentListId = _uiState.value.currentList?.id ?: return
        viewModelScope.launch {
            shoppingUseCases.renameShoppingList(currentListId, newName)
            // Update current list name in state immediately for better UX
            _uiState.update { state ->
                state.copy(currentList = state.currentList?.copy(name = newName))
            }
        }
    }

    private fun addItem(name: String, quantity: Double) {
        val currentListId = _uiState.value.currentList?.id ?: return
        viewModelScope.launch {
            shoppingUseCases.addProductToShoppingList(currentListId, name, quantity)
        }
    }

    private fun deleteItem(itemId: String) {
        val currentList = _uiState.value.currentList ?: return
        val item = currentList.items.find { it.id == itemId } ?: return
        viewModelScope.launch {
            shoppingUseCases.deleteShoppingListItem(item)
        }
    }

    private fun toggleItem(itemId: String) {
        val currentList = _uiState.value.currentList ?: return
        val item = currentList.items.find { it.id == itemId } ?: return
        viewModelScope.launch {
            shoppingUseCases.toggleShoppingListItem(itemId, !item.isCompleted)
        }
    }

    private fun finishShopping(supermarket: String?, price: Double?) {
        val currentListId = _uiState.value.currentList?.id ?: return
        viewModelScope.launch {
            shoppingUseCases.finishShopping.execute(
                listId = currentListId,
                supermarket = supermarket,
                totalAmount = price ?: 0.0
            )
            // After finishing, we could potentially reset the items completion or archive the list
            _uiState.update { it.copy(currentList = null) }
        }
    }
}
