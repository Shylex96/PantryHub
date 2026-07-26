package com.pantryhub.feature.shopping.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantryhub.core.data.repository.ShoppingListRepository
import com.pantryhub.core.domain.shopping.CreateShoppingListUseCase
import com.pantryhub.core.domain.shopping.FinishShoppingUseCase
import com.pantryhub.core.model.product.Product
import com.pantryhub.core.model.shopping.ShoppingListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val createShoppingListUseCase: CreateShoppingListUseCase,
    private val finishShoppingUseCase: FinishShoppingUseCase
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
            is ShoppingIntent.AddItem -> addItem(intent.name, intent.quantity)
            is ShoppingIntent.DeleteItem -> deleteItem(intent.itemId)
            is ShoppingIntent.ToggleItem -> toggleItem(intent.itemId)
            ShoppingIntent.FinishShopping -> finishShopping()
        }
    }

    private fun observeLists() {
        shoppingListRepository.getLists()
            .onEach { lists ->
                _uiState.update { it.copy(lists = lists) }
            }
            .launchIn(viewModelScope)
    }

    private fun createList(name: String) {
        viewModelScope.launch {
            createShoppingListUseCase(name)
        }
    }

    private fun openList(id: String) {
        itemsObservationJob?.cancel()
        
        viewModelScope.launch {
            val list = shoppingListRepository.getList(id)
            _uiState.update { it.copy(currentList = list) }
            
            // Start observing items for this specific list
            itemsObservationJob = shoppingListRepository.getItemsForList(id)
                .onEach { items ->
                    _uiState.update { state ->
                        state.copy(currentList = state.currentList?.copy(items = items))
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun addItem(name: String, quantity: Double) {
        val currentListId = _uiState.value.currentList?.id ?: return
        viewModelScope.launch {
            val newItem = ShoppingListItem(
                id = UUID.randomUUID().toString(),
                shoppingListId = currentListId,
                product = Product(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    normalizedName = name.lowercase(),
                    createdAt = Clock.System.now()
                ),
                quantity = quantity,
                addedAt = Clock.System.now()
            )
            shoppingListRepository.saveItem(newItem)
        }
    }

    private fun deleteItem(itemId: String) {
        val currentList = _uiState.value.currentList ?: return
        val item = currentList.items.find { it.id == itemId } ?: return
        viewModelScope.launch {
            shoppingListRepository.deleteItem(item)
        }
    }

    private fun toggleItem(itemId: String) {
        val currentList = _uiState.value.currentList ?: return
        val item = currentList.items.find { it.id == itemId } ?: return
        viewModelScope.launch {
            shoppingListRepository.updateItemCompletion(itemId, !item.isCompleted)
        }
    }

    private fun finishShopping() {
        val currentListId = _uiState.value.currentList?.id ?: return
        viewModelScope.launch {
            finishShoppingUseCase.execute(currentListId)
        }
    }
}
