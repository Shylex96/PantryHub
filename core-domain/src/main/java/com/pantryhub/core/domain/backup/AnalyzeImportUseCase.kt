package com.pantryhub.core.domain.backup

import com.pantryhub.core.common.util.jaroWinklerSimilarity
import com.pantryhub.core.data.repository.BackupRepository
import com.pantryhub.core.data.repository.ProductRepository
import com.pantryhub.core.model.product.Product
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Parses a JSON backup and classifies each product against what already exists:
 *  - exact normalized match -> auto-merge (counted, no decision needed)
 *  - similarity >= [SIMILARITY_THRESHOLD] but not exact -> conflict (needs user decision)
 *  - otherwise -> new product
 */
class AnalyzeImportUseCase @Inject constructor(
    private val backupRepository: BackupRepository,
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(json: String): ImportPreview {
        val data = backupRepository.deserialize(json)
        val existing = productRepository.getProducts().first()
        val existingKeys = existing.mapTo(HashSet()) { it.normalizedName }

        var newCount = 0
        var autoMergeCount = 0
        val conflicts = mutableListOf<ProductConflict>()

        data.products.forEach { imported ->
            if (existingKeys.contains(imported.normalizedName)) {
                autoMergeCount++
                return@forEach
            }

            var best: Product? = null
            var bestSimilarity = 0.0
            existing.forEach { candidate ->
                val similarity = jaroWinklerSimilarity(imported.normalizedName, candidate.normalizedName)
                if (similarity > bestSimilarity) {
                    bestSimilarity = similarity
                    best = candidate
                }
            }

            val match = best
            if (match != null && bestSimilarity >= SIMILARITY_THRESHOLD) {
                conflicts += ProductConflict(
                    importedProductId = imported.id,
                    importedName = imported.name,
                    existingProductId = match.id,
                    existingName = match.name,
                    similarityPercent = (bestSimilarity * 100).toInt()
                )
            } else {
                newCount++
            }
        }

        return ImportPreview(
            data = data,
            newProductCount = newCount,
            autoMergeCount = autoMergeCount,
            conflicts = conflicts,
            categoryCount = data.categories.size,
            listCount = data.shoppingLists.size,
            purchaseCount = data.purchases.size
        )
    }

    companion object {
        const val SIMILARITY_THRESHOLD = 0.8
    }
}
