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

    /** Category management, reached from the Products tab header. */
    @Serializable
    data object Categories : Destination

    @Serializable
    data object Notes : Destination

    @Serializable
    data object Settings : Destination

    @Serializable
    data object ImportExport : Destination

    @Serializable
    data object Help : Destination
}
