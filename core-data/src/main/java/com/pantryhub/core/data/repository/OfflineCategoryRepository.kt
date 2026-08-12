package com.pantryhub.core.data.repository

import com.pantryhub.core.database.dao.CategoryDao
import com.pantryhub.core.database.mapper.asDomainModel
import com.pantryhub.core.database.mapper.asEntity
import com.pantryhub.core.model.category.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineCategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { entities ->
            entities.map { it.asDomainModel() }
        }
    }

    override suspend fun getCategory(id: String): Category? {
        return categoryDao.getCategoryById(id)?.asDomainModel()
    }

    override suspend fun saveCategory(category: Category, normalizedName: String) {
        categoryDao.insertCategory(category.asEntity(normalizedName))
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategoryById(category.id)
    }
}
