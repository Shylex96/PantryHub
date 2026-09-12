package com.pantryhub.feature.products.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantryhub.core.common.util.toStorageName
import com.pantryhub.core.domain.category.CategoryUseCases
import com.pantryhub.core.domain.product.ProductUseCases
import com.pantryhub.core.model.category.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/** One category with how many products use it. */
data class CategoryRow(
    val category: Category,
    val productCount: Int
)

data class CategoriesUiState(
    val rows: List<CategoryRow> = emptyList(),
    val uncategorizedCount: Int = 0,
    val isLoading: Boolean = true
)

/** Backs the Categories screen (docs/04_UX_Guidelines.md "Category Browsing"). */
@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases,
    private val productUseCases: ProductUseCases
) : ViewModel() {

    val uiState: StateFlow<CategoriesUiState> = combine(
        categoryUseCases.getCategories(),
        productUseCases.getProducts()
    ) { categories, products ->
        val counts = products.groupingBy { it.categoryId }.eachCount()
        val knownIds = categories.map { it.id }.toSet()
        CategoriesUiState(
            rows = categories.map { CategoryRow(it, counts[it.id] ?: 0) },
            uncategorizedCount = products.count { it.categoryId == null || it.categoryId !in knownIds },
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CategoriesUiState())

    fun create(name: String) {
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

    fun rename(id: String, name: String) {
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

    /** Products in the category are kept and become uncategorised. */
    fun delete(category: Category) {
        viewModelScope.launch { categoryUseCases.deleteCategory(category) }
    }
}
