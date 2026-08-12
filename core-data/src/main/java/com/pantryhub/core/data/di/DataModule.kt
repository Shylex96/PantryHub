package com.pantryhub.core.data.di

import com.pantryhub.core.data.repository.BackupRepository
import com.pantryhub.core.data.repository.CategoryRepository
import com.pantryhub.core.data.repository.OfflineBackupRepository
import com.pantryhub.core.data.repository.NoteRepository
import com.pantryhub.core.data.repository.OfflineCategoryRepository
import com.pantryhub.core.data.repository.OfflineNoteRepository
import com.pantryhub.core.data.repository.OfflineProductRepository
import com.pantryhub.core.data.repository.OfflinePurchaseRepository
import com.pantryhub.core.data.repository.OfflineShoppingListRepository
import com.pantryhub.core.data.repository.DataStoreSettingsRepository
import com.pantryhub.core.data.repository.ProductRepository
import com.pantryhub.core.data.repository.PurchaseRepository
import com.pantryhub.core.data.repository.SettingsRepository
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
    abstract fun bindCategoryRepository(
        offlineCategoryRepository: OfflineCategoryRepository
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindBackupRepository(
        offlineBackupRepository: OfflineBackupRepository
    ): BackupRepository

    @Binds
    @Singleton
    abstract fun bindNoteRepository(
        offlineNoteRepository: OfflineNoteRepository
    ): NoteRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        dataStoreSettingsRepository: DataStoreSettingsRepository
    ): SettingsRepository

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
