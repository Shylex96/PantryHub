package com.pantryhub.core.designsystem.ui.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StickyNote2

/**
 * PantryHub Icons.
 * Centralized mapping of material icons to semantic names.
 */
object PantryIcons {
    val Add = Icons.Default.Add
    // Chevron-style back arrow ("<") to match the product mockup.
    val Back = Icons.AutoMirrored.Filled.KeyboardArrowLeft
    val Check = Icons.Default.Check
    val Delete = Icons.Default.Delete
    val Edit = Icons.Default.Edit
    val Info = Icons.Default.Info
    val Search = Icons.Default.Search
    val Close = Icons.Default.Close
    val Favorite = Icons.Default.Star
    val FavoriteBorder = Icons.Default.StarBorder
    val ExpandMore = Icons.Default.KeyboardArrowDown
    val ExpandLess = Icons.Default.KeyboardArrowUp

    // Navigation / feature icons (used in the bottom bar, empty states, cards).
    val Lists = Icons.Default.Checklist
    val Products = Icons.Default.Inventory2
    val Notes = Icons.Default.StickyNote2
    val Settings = Icons.Default.Settings
}
