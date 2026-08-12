package com.pantryhub.feature.products.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantryhub.core.common.util.toComparisonKey
import com.pantryhub.core.common.util.toStorageName
import com.pantryhub.core.domain.category.CategoryUseCases
import com.pantryhub.core.domain.product.ProductUseCases
import com.pantryhub.core.model.category.Category
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
            is ProductsIntent.DeleteProduct -> deleteProduct(intent.productId)
            is ProductsIntent.UpdateCreateInput -> _uiState.update { it.copy(createInput = intent.input) }
            is ProductsIntent.CreateProduct -> createProduct(intent.name)
            ProductsIntent.ToggleSearchMode -> _uiState.update {
                it.copy(isSearchMode = !it.isSearchMode, searchQuery = "")
            }
            is ProductsIntent.UpdateProductDetails ->
                updateProductDetails(intent.productId, intent.categoryId, intent.aliases)
            is ProductsIntent.SelectCategoryFilter -> _uiState.update {
                it.copy(selectedCategoryId = intent.categoryId)
            }
            is ProductsIntent.SetNewProductCategory -> _uiState.update {
                it.copy(newProductCategoryId = intent.categoryId)
            }
            ProductsIntent.OpenCategoryManager -> _uiState.update { it.copy(isManagingCategories = true) }
            ProductsIntent.CloseCategoryManager -> _uiState.update { it.copy(isManagingCategories = false) }
            is ProductsIntent.CreateCategory -> createCategory(intent.name)
            is ProductsIntent.RenameCategory -> renameCategory(intent.id, intent.name)
            is ProductsIntent.DeleteCategory -> removeCategory(intent.category)
        }
    }

    private fun observeCategories() {
        categoryUseCases.getCategories()
            .onEach { categories -> _uiState.update { it.copy(categories = categories) } }
            .launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeProducts() {
        _uiState
            .map { it.searchQuery to it.selectedCategoryId }
            .distinctUntilChanged()
            .debounce(300.milliseconds)
            .onEach { _uiState.update { it.copy(isLoading = true) } }
            .flatMapLatest { (query, categoryId) ->
                val source = when {
                    categoryId != null -> categoryUseCases.getProductsByCategory(categoryId)
                    query.isBlank() -> productUseCases.getProducts()
                    else -> productUseCases.searchProducts(query)
                }
                source.map { products ->
                    // When both a category filter and a text query are active,
                    // filter the category results by the query on the client side.
                    val filtered = if (categoryId != null && query.isNotBlank()) {
                        products.filter { it.name.contains(query.trim(), ignoreCase = true) }
                    } else {
                        products
                    }
                    filtered.sortedWith(
                        compareByDescending<Product> { it.isFavorite }
                            .thenBy { it.name.lowercase() }
                    )
                }
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
            val existing = productUseCases.detectDuplicateProduct.execute(name)
            if (existing == null) {
                val newProduct = Product(
                    id = UUID.randomUUID().toString(),
                    name = storageName,
                    normalizedName = name.toComparisonKey(),
                    categoryId = _uiState.value.newProductCategoryId,
                    createdAt = Clock.System.now()
                )
                productUseCases.saveProduct(newProduct)
            }
            _uiState.update { it.copy(createInput = "") }
        }
    }

    private fun updateProductDetails(
        productId: String,
        categoryId: String?,
        aliases: List<String>
    ) {
        viewModelScope.launch {
            val product = _uiState.value.products.find { it.id == productId } ?: return@launch
            productUseCases.saveProduct(product.copy(categoryId = categoryId, aliases = aliases))
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

    private fun createCategory(name: String) {
        val storageName = name.toStorageName()
        if (storageName.isEmpty()) return

        viewModelScope.launch {
            val existing = categoryUseCases.detectDuplicateCategory.execute(name)
            if (existing == null) {
                categoryUseCases.saveCategory(
                    Category(id = UUID.randomUUID().toString(), name = storageName)
                )
            }
        }
    }

    private fun renameCategory(id: String, name: String) {
        val storageName = name.toStorageName()
        if (storageName.isEmpty()) return

        viewModelScope.launch {
            // Allow the rename unless it collides with a *different* category.
            val existing = categoryUseCases.detectDuplicateCategory.execute(name)
            if (existing == null || existing.id == id) {
                categoryUseCases.saveCategory(Category(id = id, name = storageName))
            }
        }
    }

    private fun removeCategory(category: Category) {
        viewModelScope.launch {
            categoryUseCases.deleteCategory(category)
            _uiState.update {
                it.copy(
                    selectedCategoryId = it.selectedCategoryId.takeIf { id -> id != category.id },
                    newProductCategoryId = it.newProductCategoryId.takeIf { id -> id != category.id }
                )
            }
        }
    }
}
