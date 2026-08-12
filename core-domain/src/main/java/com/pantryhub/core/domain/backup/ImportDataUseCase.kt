package com.pantryhub.core.domain.backup

import com.pantryhub.core.common.util.toComparisonKey
import com.pantryhub.core.data.repository.CategoryRepository
import com.pantryhub.core.data.repository.ProductRepository
import com.pantryhub.core.data.repository.PurchaseRepository
import com.pantryhub.core.data.repository.ShoppingListRepository
import com.pantryhub.core.model.backup.BackupData
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ImportDataUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val purchaseRepository: PurchaseRepository
) {
    /**
     * Merges a backup into the local database. Categories and products are merged by
     * normalized name; their ids are remapped so shopping-list items always reference a
     * product that actually exists (avoids foreign-key failures on both fresh and
     * merge-into-existing imports).
     */
    /**
     * @param mergeDecisions maps an imported product id to an existing product id it should
     * be merged into (from the conflict-resolution step). Products not listed here are
     * imported as new (unless they exactly match by normalized name).
     */
    suspend operator fun invoke(
        data: BackupData,
        mergeDecisions: Map<String, String> = emptyMap()
    ) {
        // 1. Categories — merge by normalized name, remembering how each imported id resolves.
        val existingCategoriesByKey = categoryRepository.getCategories().first()
            .associateBy { it.name.toComparisonKey() }
        val categoryIdRemap = mutableMapOf<String, String>()
        data.categories.forEach { imported ->
            val key = imported.name.toComparisonKey()
            val existing = existingCategoriesByKey[key]
            if (existing != null) {
                categoryIdRemap[imported.id] = existing.id
            } else {
                categoryRepository.saveCategory(imported, key)
                categoryIdRemap[imported.id] = imported.id
            }
        }

        // 2. Products — merge by normalized name; remap category id; remember id resolution.
        val existingProductsByKey = productRepository.getProducts().first()
            .associateBy { it.normalizedName }
        val productIdRemap = mutableMapOf<String, String>()
        val resolvedProductIds = existingProductsByKey.values.map { it.id }.toMutableSet()
        data.products.forEach { imported ->
            val existing = existingProductsByKey[imported.normalizedName]
            val forcedMergeId = mergeDecisions[imported.id]
            when {
                existing != null -> productIdRemap[imported.id] = existing.id
                forcedMergeId != null -> productIdRemap[imported.id] = forcedMergeId
                else -> {
                    val remappedCategoryId = imported.categoryId?.let { categoryIdRemap[it] ?: it }
                    productRepository.saveProduct(imported.copy(categoryId = remappedCategoryId))
                    productIdRemap[imported.id] = imported.id
                    resolvedProductIds.add(imported.id)
                }
            }
        }

        // 3. Shopping lists + items — items reference the resolved product id, and are only
        // saved when that product exists, so foreign keys always hold.
        data.shoppingLists.forEach { list ->
            shoppingListRepository.saveList(list)
            list.items.forEach { item ->
                val resolvedProductId = productIdRemap[item.product.id] ?: item.product.id
                if (resolvedProductId in resolvedProductIds) {
                    shoppingListRepository.saveItem(
                        item.copy(product = item.product.copy(id = resolvedProductId))
                    )
                }
            }
        }

        // 4. Purchases (totals; line items are not part of the export yet).
        data.purchases.forEach { purchase ->
            purchaseRepository.savePurchase(purchase)
        }
    }
}
