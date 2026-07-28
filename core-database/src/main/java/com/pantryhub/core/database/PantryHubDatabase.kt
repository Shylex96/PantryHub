package com.pantryhub.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pantryhub.core.database.dao.ProductDao
import com.pantryhub.core.database.dao.PurchaseDao
import com.pantryhub.core.database.dao.ShoppingListDao
import com.pantryhub.core.database.entity.CategoryEntity
import com.pantryhub.core.database.entity.ProductEntity
import com.pantryhub.core.database.entity.PurchaseEntity
import com.pantryhub.core.database.entity.PurchaseItemEntity
import com.pantryhub.core.database.entity.ShoppingItemEntity
import com.pantryhub.core.database.entity.ShoppingListEntity
import com.pantryhub.core.database.util.InstantConverter
import com.pantryhub.core.database.util.ShoppingListTypeConverter

@Database(
    entities = [
        ProductEntity::class,
        CategoryEntity::class,
        ShoppingListEntity::class,
        ShoppingItemEntity::class,
        PurchaseEntity::class,
        PurchaseItemEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(InstantConverter::class, ShoppingListTypeConverter::class)
abstract class PantryHubDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun purchaseDao(): PurchaseDao
}
