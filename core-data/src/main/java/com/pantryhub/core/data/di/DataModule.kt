package com.pantryhub.core.data.di

import com.pantryhub.core.data.repository.OfflineProductRepository
import com.pantryhub.core.data.repository.OfflinePurchaseRepository
import com.pantryhub.core.data.repository.OfflineShoppingListRepository
import com.pantryhub.core.data.repository.ProductRepository
import com.pantryhub.core.data.repository.PurchaseRepository
import com.pantryhub.core.data.repository.ShoppingListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        offlineProductRepository: OfflineProductRepository
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindShoppingListRepository(
        offlineShoppingListRepository: OfflineShoppingListRepository
    ): ShoppingListRepository

    @Binds
    @Singleton
    abstract fun bindPurchaseRepository(
        offlinePurchaseRepository: OfflinePurchaseRepository
    ): PurchaseRepository
}
