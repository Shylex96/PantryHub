package com.pantryhub.core.domain.category

import com.pantryhub.core.common.util.toComparisonKey
import com.pantryhub.core.data.repository.CategoryRepository
import com.pantryhub.core.model.category.Category
import javax.inject.Inject

class SaveCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    /**
     * Creates or renames a category. The normalized name (accent/case-insensitive)
     * is derived from the display name and stored for duplicate detection.
     */
    suspend operator fun invoke(category: Category) {
        repository.saveCategory(category, category.name.toComparisonKey())
    }
}
