package com.pantryhub.core.database.util

import androidx.room.TypeConverter
import com.pantryhub.core.model.shopping.ShoppingListType

class ShoppingListTypeConverter {
    @TypeConverter
    fun fromString(value: String): ShoppingListType = ShoppingListType.valueOf(value)

    @TypeConverter
    fun toString(type: ShoppingListType): String = type.name
}
