package com.pantryhub.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination {
    @Serializable
    data object ShoppingLists : Destination

    @Serializable
    data class ShoppingListDetail(val listId: String) : Destination

    @Serializable
    data class ShoppingMode(val listId: String) : Destination

    @Serializable
    data object Products : Destination

    @Serializable
    data object Notes : Destination

    @Serializable
    data object Settings : Destination

    @Serializable
    data object ImportExport : Destination

    @Serializable
    data object Help : Destination
}
