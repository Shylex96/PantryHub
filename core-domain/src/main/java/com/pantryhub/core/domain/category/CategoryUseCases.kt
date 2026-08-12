package com.pantryhub.core.domain.category

import javax.inject.Inject

class CategoryUseCases @Inject constructor(
    val getCategories: GetCategoriesUseCase,
    val saveCategory: SaveCategoryUseCase,
    val deleteCategory: DeleteCategoryUseCase,
    val detectDuplicateCategory: DetectDuplicateCategoryUseCase,
    val getProductsByCategory: GetProductsByCategoryUseCase
)
