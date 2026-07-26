package com.pantryhub.core.data.repository

import com.pantryhub.core.database.dao.PurchaseDao
import com.pantryhub.core.database.mapper.asEntity
import com.pantryhub.core.database.mapper.asPurchaseItem
import com.pantryhub.core.model.purchase.Purchase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflinePurchaseRepository @Inject constructor(
    private val purchaseDao: PurchaseDao
) : PurchaseRepository {
    override fun getPurchases(): Flow<List<Purchase>> {
        // Simple mapping for now, items will be empty as they are in a different table
        return purchaseDao.getAllPurchases().map { entities ->
            entities.map { entity ->
                Purchase(
                    id = entity.id,
                    date = entity.date,
                    totalAmount = entity.totalAmount,
                    supermarket = entity.supermarket,
                    items = emptyList()
                )
            }
        }
    }

    override suspend fun savePurchase(purchase: Purchase) {
        val purchaseEntity = purchase.asEntity()
        val itemEntities = purchase.items.map { it.asPurchaseItem(purchase.id) }
        purchaseDao.insertPurchaseWithItems(purchaseEntity, itemEntities)
    }
}
