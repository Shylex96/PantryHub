package com.pantryhub.core.domain.category

import com.pantryhub.core.data.repository.CategoryRepository
import com.pantryhub.core.data.repository.ProductRepository
import com.pantryhub.core.model.category.Category
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
) {
    /**
     * Deletes a category. Products that referenced it are not deleted; their
     * category assignment is cleared so no dangling reference remains.
     */
    suspend operator fun invoke(category: Category) {
        productRepository.clearCategoryFromProducts(category.id)
        categoryRepository.deleteCategory(category)
    }
}
