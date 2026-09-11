package com.pantryhub.core.designsystem.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class PantrySpacing(
    val default: Dp = 0.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 24.dp,
    val xxl: Dp = 32.dp,
    /** Horizontal screen margin (docs/05_Design_System.md §6.1). */
    val screen: Dp = 20.dp
)

/**
 * Corner radii of the composition system (docs/05_Design_System.md §6).
 * Material's `Shapes` only has three steps, so the redesign's radii live here.
 */
data class PantryRadius(
    val iconButton: Dp = 12.dp,
    val tile: Dp = 14.dp,
    val row: Dp = 16.dp,
    val card: Dp = 20.dp,
    val sheet: Dp = 28.dp
)

data class PantryIconSize(
    val sm: Dp = 16.dp,
    val md: Dp = 24.dp,
    val lg: Dp = 32.dp,
    val xl: Dp = 48.dp
)

data class PantryElevation(
    val none: Dp = 0.dp,
    val low: Dp = 2.dp,
    val medium: Dp = 4.dp,
    val high: Dp = 8.dp
)

data class PantryTouchTarget(
    val minimum: Dp = 48.dp
)

val LocalPantrySpacing = androidx.compose.runtime.staticCompositionLocalOf { PantrySpacing() }
val LocalPantryRadius = androidx.compose.runtime.staticCompositionLocalOf { PantryRadius() }
val LocalPantryIconSize = androidx.compose.runtime.staticCompositionLocalOf { PantryIconSize() }
val LocalPantryElevation = androidx.compose.runtime.staticCompositionLocalOf { PantryElevation() }
val LocalPantryTouchTarget = androidx.compose.runtime.staticCompositionLocalOf { PantryTouchTarget() }
