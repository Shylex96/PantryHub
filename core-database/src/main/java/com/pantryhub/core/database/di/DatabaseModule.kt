package com.pantryhub.core.database.di

import android.content.Context
import androidx.room.Room
import com.pantryhub.core.database.PantryHubDatabase
import com.pantryhub.core.database.dao.ProductDao
import com.pantryhub.core.database.dao.PurchaseDao
import com.pantryhub.core.database.dao.ShoppingListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PantryHubDatabase {
        return Room.databaseBuilder(
            context,
            PantryHubDatabase::class.java,
            "pantryhub-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideProductDao(database: PantryHubDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideShoppingListDao(database: PantryHubDatabase): ShoppingListDao {
        return database.shoppingListDao()
    }

    @Provides
    fun providePurchaseDao(database: PantryHubDatabase): PurchaseDao {
        return database.purchaseDao()
    }
}
