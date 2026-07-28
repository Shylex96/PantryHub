package com.pantryhub.feature.shopping.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantryhub.core.domain.product.ProductUseCases
import com.pantryhub.core.domain.shopping.ShoppingUseCases
import com.pantryhub.core.model.shopping.ShoppingListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val shoppingUseCases: ShoppingUseCases,
    private val productUseCases: ProductUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingUiState())
    val uiState: StateFlow<ShoppingUiState> = _uiState.asStateFlow()

    private var itemsObservationJob: Job? = null

    init {
        observeLists()
        observeProductQuery()
    }

    fun handleIntent(intent: ShoppingIntent) {
        when (intent) {
            ShoppingIntent.LoadLists -> observeLists()
            is ShoppingIntent.CreateList -> createList(intent.name)
            is ShoppingIntent.OpenList -> openList(intent.id)
            is ShoppingIntent.DeleteList -> deleteList(intent.id)
            is ShoppingIntent.RenameList -> renameList(intent.newName)
            is ShoppingIntent.UpdateProductQuery -> _uiState.update { it.copy(productQuery = intent.query) }
            is ShoppingIntent.AddItem -> addItem(intent.name, intent.quantity)
            is ShoppingIntent.DeleteItem -> deleteItem(intent.itemId)
            is ShoppingIntent.ToggleItem -> toggleItem(intent.itemId)
            is ShoppingIntent.ToggleFavorite -> toggleFavorite(intent.productId, intent.isFavorite)
            is ShoppingIntent.FinishWithData -> finishShopping(intent.supermarket, intent.price)
        }
    }

    @OptIn(FlowPreview::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    private fun observeProductQuery() {
        _uiState
            .map { it.productQuery }
            .distinctUntilChanged()
            .debounce(200.milliseconds)
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    kotlinx.coroutines.flow.flowOf(emptyList())
                } else {
                    productUseCases.searchProducts(query)
                }
            }
            .onEach { suggestions ->
                _uiState.update { it.copy(suggestions = suggestions) }
            }
            .launchIn(viewModelScope)
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
                .map { items ->
                    items.sortedWith(
                        compareByDescending<ShoppingListItem> { it.product.isFavorite }
                            .thenBy { it.product.name.lowercase() }
                    )
                }
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

    private fun toggleFavorite(productId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            productUseCases.toggleFavoriteProduct(productId, isFavorite)
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
            _uiState.update { it.copy(currentList = null) }
        }
    }
}
