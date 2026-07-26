package com.pantryhub.core.data.repository

import com.pantryhub.core.model.purchase.Purchase
import kotlinx.coroutines.flow.Flow

interface PurchaseRepository {
    fun getPurchases(): Flow<List<Purchase>>
    suspend fun savePurchase(purchase: Purchase)
}
