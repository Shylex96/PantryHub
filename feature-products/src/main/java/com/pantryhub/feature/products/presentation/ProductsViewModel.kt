package com.pantryhub.feature.products.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantryhub.core.common.util.toComparisonKey
import com.pantryhub.core.common.util.toStorageName
import com.pantryhub.core.domain.category.CategoryUseCases
import com.pantryhub.core.domain.product.ProductUseCases
import com.pantryhub.core.model.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
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
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productUseCases: ProductUseCases,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    init {
        observeProducts()
        observeCategories()
    }

    fun handleIntent(intent: ProductsIntent) {
        when (intent) {
            ProductsIntent.LoadProducts -> { /* Handled by initial observation */ }
            is ProductsIntent.Search -> updateSearchQuery(intent.query)
            is ProductsIntent.ToggleFavorite -> toggleFavorite(intent.productId, intent.isFavorite)
            is ProductsIntent.DeleteProduct -> deleteProducts(setOf(intent.productId))
            is ProductsIntent.CreateProduct -> createProduct(intent.name, intent.categoryId)
            is ProductsIntent.UpdateProductDetails ->
                updateProductDetails(intent.productId, intent.name, intent.categoryId, intent.aliases)
            is ProductsIntent.AssignCategory -> assignCategory(intent.productIds, intent.categoryId)
            is ProductsIntent.DeleteProducts -> deleteProducts(intent.productIds)
            is ProductsIntent.ApplyFilter -> _uiState.update {
                it.copy(filter = intent.filter, sort = intent.sort)
            }
            ProductsIntent.ResetFilter -> _uiState.update {
                it.copy(filter = ProductFilter.All, sort = ProductSort.CATEGORY)
            }
        }
    }

    private fun observeCategories() {
        categoryUseCases.getCategories()
            .onEach { categories ->
                _uiState.update { state ->
                    // A filter pointing at a category that no longer exists falls back to "All"
                    // (categories are managed on their own screen).
                    val filter = state.filter
                    val stillValid = filter !is ProductFilter.ByCategory ||
                        categories.any { it.id == filter.categoryId }
                    state.copy(
                        categories = categories,
                        filter = if (stillValid) filter else ProductFilter.All
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeProducts() {
        // The category filter and the sort are applied on the UI side (grouping needs the
        // full set anyway, e.g. for per-category counts in the filter sheet); only the
        // text search goes to the database.
        _uiState
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(300.milliseconds)
            .onEach { _uiState.update { it.copy(isLoading = true) } }
            .flatMapLatest { query ->
                if (query.isBlank()) productUseCases.getProducts() else productUseCases.searchProducts(query)
            }
            .map { products -> products.sortedBy { it.name.lowercase() } }
            .onEach { products ->
                _uiState.update { it.copy(products = products, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    private fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    private fun createProduct(name: String, categoryId: String?) {
        val storageName = name.toStorageName()
        if (storageName.isEmpty()) return

        viewModelScope.launch {
            val existing = productUseCases.detectDuplicateProduct.execute(name)
            if (existing == null) {
                val newProduct = Product(
                    id = UUID.randomUUID().toString(),
                    name = storageName,
                    normalizedName = name.toComparisonKey(),
                    categoryId = categoryId,
                    createdAt = Clock.System.now()
                )
                productUseCases.saveProduct(newProduct)
            }
        }
    }

    private fun updateProductDetails(
        productId: String,
        name: String,
        categoryId: String?,
        aliases: List<String>
    ) {
        viewModelScope.launch {
            val product = _uiState.value.products.find { it.id == productId } ?: return@launch
            val storageName = name.toStorageName()
            // Keep the old name when the new one is blank or collides with another product.
            val renamed = storageName.isNotEmpty() && storageName != product.name
            val collides = renamed &&
                productUseCases.detectDuplicateProduct.execute(name)?.let { it.id != productId } == true
            val finalName = if (renamed && !collides) storageName else product.name
            val finalKey = if (renamed && !collides) name.toComparisonKey() else product.normalizedName
            productUseCases.saveProduct(
                product.copy(
                    name = finalName,
                    normalizedName = finalKey,
                    categoryId = categoryId,
                    aliases = aliases
                )
            )
        }
    }

    /** Moves every selected product to [categoryId] (null = no category) in one go. */
    private fun assignCategory(productIds: Set<String>, categoryId: String?) {
        viewModelScope.launch {
            _uiState.value.products
                .filter { it.id in productIds && it.categoryId != categoryId }
                .forEach { productUseCases.saveProduct(it.copy(categoryId = categoryId)) }
        }
    }

    private fun toggleFavorite(productId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            productUseCases.toggleFavoriteProduct(productId, isFavorite)
        }
    }

    private fun deleteProducts(productIds: Set<String>) {
        viewModelScope.launch {
            _uiState.value.products
                .filter { it.id in productIds }
                .forEach { productUseCases.deleteProduct(it) }
        }
    }
}
