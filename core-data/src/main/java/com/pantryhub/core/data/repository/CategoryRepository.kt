package com.pantryhub.core.data.repository

import com.pantryhub.core.model.category.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<Category>>
    suspend fun getCategory(id: String): Category?

    /**
     * Persists a category. [normalizedName] is computed in the domain layer
     * (accent/case-insensitive key) and stored for duplicate detection.
     */
    suspend fun saveCategory(category: Category, normalizedName: String)
    suspend fun deleteCategory(category: Category)
}
