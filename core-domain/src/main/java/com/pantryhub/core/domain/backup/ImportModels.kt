package com.pantryhub.core.domain.backup

import com.pantryhub.core.model.backup.BackupData

/**
 * A product in the backup that is similar (but not identical) to an existing product,
 * so the user must decide whether it's the same item or a new one.
 */
data class ProductConflict(
    val importedProductId: String,
    val importedName: String,
    val existingProductId: String,
    val existingName: String,
    val similarityPercent: Int
)

/**
 * The result of analyzing a backup before importing it: counts plus the ambiguous
 * product conflicts that need a user decision. Holds the parsed [data] so it can be
 * imported once the user confirms.
 */
data class ImportPreview(
    val data: BackupData,
    val newProductCount: Int,
    val autoMergeCount: Int,
    val conflicts: List<ProductConflict>,
    val categoryCount: Int,
    val listCount: Int,
    val purchaseCount: Int,
    val noteCount: Int
)
