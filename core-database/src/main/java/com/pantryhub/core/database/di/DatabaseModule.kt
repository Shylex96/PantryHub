package com.pantryhub.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pantryhub.core.database.PantryHubDatabase
import com.pantryhub.core.database.dao.CategoryDao
import com.pantryhub.core.database.dao.ProductDao
import com.pantryhub.core.database.dao.PurchaseDao
import com.pantryhub.core.database.dao.ShoppingListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * v2 -> v3: adds product aliases (display + normalized) for alias-based search.
 * ADD COLUMN with a matching default keeps existing rows intact.
 */
private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE products ADD COLUMN aliases TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE products ADD COLUMN normalized_aliases TEXT NOT NULL DEFAULT ''")
    }
}

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
            .addMigrations(MIGRATION_2_3)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideProductDao(database: PantryHubDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideCategoryDao(database: PantryHubDatabase): CategoryDao {
        return database.categoryDao()
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
