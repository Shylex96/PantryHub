package com.pantryhub.core.model.shopping

import kotlinx.serialization.Serializable

@Serializable
enum class ShoppingListType {
    REGULAR,
    TEMPORARY,
    SHARED
}
