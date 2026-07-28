package com.pantryhub.feature.products.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantryhub.core.common.util.toComparisonKey
import com.pantryhub.core.common.util.toStorageName
import com.pantryhub.core.domain.product.ProductUseCases
import com.pantryhub.core.model.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productUseCases: ProductUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    init {
        observeProducts()
    }

    fun handleIntent(intent: ProductsIntent) {
        when (intent) {
            ProductsIntent.LoadProducts -> { /* Handled by initial observation */ }
            is ProductsIntent.Search -> updateSearchQuery(intent.query)
            is ProductsIntent.ToggleFavorite -> toggleFavorite(intent.productId, intent.isFavorite)
            is ProductsIntent.DeleteProduct -> deleteProduct(intent.productId)
            is ProductsIntent.UpdateCreateInput -> _uiState.update { it.copy(createInput = intent.input) }
            is ProductsIntent.CreateProduct -> createProduct(intent.name)
            ProductsIntent.ToggleSearchMode -> _uiState.update { 
                it.copy(isSearchMode = !it.isSearchMode, searchQuery = "") 
            }
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeProducts() {
        _uiState
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(300.milliseconds)
            .onEach { _uiState.update { it.copy(isLoading = true) } }
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    productUseCases.getProducts()
                } else {
                    productUseCases.searchProducts(query)
                }
            }
            .map { products ->
                products.sortedWith(
                    compareByDescending<Product> { it.isFavorite }
                        .thenBy { it.name.lowercase() }
                )
            }
            .onEach { products ->
                _uiState.update { it.copy(products = products, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    private fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    private fun createProduct(name: String) {
        val storageName = name.toStorageName()
        if (storageName.isEmpty()) return

        viewModelScope.launch {
            // Check if exists
            val existing = productUseCases.detectDuplicateProduct.execute(name)
            if (existing == null) {
                val newProduct = Product(
                    id = UUID.randomUUID().toString(),
                    name = storageName,
                    normalizedName = name.toComparisonKey(),
                    createdAt = Clock.System.now()
                )
                productUseCases.saveProduct(newProduct)
            }
            _uiState.update { it.copy(createInput = "") }
        }
    }

    private fun toggleFavorite(productId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            productUseCases.toggleFavoriteProduct(productId, isFavorite)
        }
    }

    private fun deleteProduct(productId: String) {
        viewModelScope.launch {
            val product = _uiState.value.products.find { it.id == productId } ?: return@launch
            productUseCases.deleteProduct(product)
        }
    }
}
