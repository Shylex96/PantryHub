package com.pantryhub.core.domain.category

import com.pantryhub.core.data.repository.CategoryRepository
import com.pantryhub.core.model.category.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke(): Flow<List<Category>> = repository.getCategories()
}
