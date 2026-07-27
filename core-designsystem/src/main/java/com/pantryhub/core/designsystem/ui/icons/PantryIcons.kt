package com.pantryhub.core.designsystem.ui.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star

/**
 * PantryHub Icons.
 * Centralized mapping of material icons to semantic names.
 */
object PantryIcons {
    val Add = Icons.Default.Add
    val Back = Icons.AutoMirrored.Filled.ArrowBack
    val Check = Icons.Default.Check
    val Delete = Icons.Default.Delete
    val Edit = Icons.Default.Edit
    val Info = Icons.Default.Info
    val Search = Icons.Default.Search
    
    // Navigation
    val Lists = Icons.AutoMirrored.Filled.List
    val Products = Icons.Default.ShoppingCart
    val Notes = Icons.Default.Star
    val Settings = Icons.Default.Settings
}
