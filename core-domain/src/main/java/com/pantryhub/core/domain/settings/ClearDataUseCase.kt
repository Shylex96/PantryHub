package com.pantryhub.core.domain.settings

import com.pantryhub.core.data.repository.CategoryRepository
import com.pantryhub.core.data.repository.NoteRepository
import com.pantryhub.core.data.repository.ProductRepository
import com.pantryhub.core.data.repository.PurchaseRepository
import com.pantryhub.core.data.repository.ShoppingListRepository
import javax.inject.Inject

/** What Settings > Manage data can wipe. */
enum class DataScope {
    /** Every shopping list (with its items). */
    LISTS,
    /** Every product; also empties every list, since items point at products. */
    PRODUCTS,
    /** Every category; products are kept and become uncategorised. */
    CATEGORIES,
    /** Every note. */
    NOTES,
    /** The purchase history recorded when finishing a shop. */
    HISTORY,
    /** Unmark every favorite product (nothing is deleted). */
    FAVORITES
}

/**
 * Bulk deletion for Settings > Manage data. Scopes run in dependency order so nothing is
 * left dangling: lists → products → categories → notes → history → favorites.
 */
class ClearDataUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val noteRepository: NoteRepository,
    private val purchaseRepository: PurchaseRepository
) {
    suspend operator fun invoke(scopes: Set<DataScope>) {
        if (DataScope.LISTS in scopes || DataScope.PRODUCTS in scopes) {
            shoppingListRepository.deleteAllLists()
        }
        if (DataScope.PRODUCTS in scopes) {
            productRepository.deleteAllProducts()
        }
        if (DataScope.CATEGORIES in scopes) {
            if (DataScope.PRODUCTS !in scopes) productRepository.clearAllCategoryAssignments()
            categoryRepository.deleteAllCategories()
        }
        if (DataScope.NOTES in scopes) {
            noteRepository.deleteAllNotes()
        }
        if (DataScope.HISTORY in scopes) {
            purchaseRepository.deleteAllPurchases()
        }
        if (DataScope.FAVORITES in scopes && DataScope.PRODUCTS !in scopes) {
            productRepository.clearAllFavorites()
        }
    }
}
