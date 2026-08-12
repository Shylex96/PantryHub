package com.pantryhub.core.domain.category

import com.pantryhub.core.common.util.toComparisonKey
import com.pantryhub.core.data.repository.CategoryRepository
import com.pantryhub.core.model.category.Category
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DetectDuplicateCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    /**
     * Returns an existing category whose normalized name matches [name],
     * or null. Used to avoid creating accent/case duplicates ("Fruta"/"fruta").
     */
    suspend fun execute(name: String): Category? {
        val key = name.toComparisonKey()
        if (key.isEmpty()) return null
        return repository.getCategories().first().find { it.name.toComparisonKey() == key }
    }
}
