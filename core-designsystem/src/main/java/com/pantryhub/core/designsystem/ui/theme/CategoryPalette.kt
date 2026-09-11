package com.pantryhub.core.designsystem.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Ordered, mode-aware palette used to color categories deterministically by their position
 * in the category list. Screens resolve `palette[index % palette.size]`; an unknown or
 * missing category uses [uncategorizedDotColor].
 */
@Composable
fun categoryPalette(): List<Color> {
    val extended = PantryHubTheme.extendedColors
    return listOf(
        extended.categoryVegetables,
        extended.categoryFruit,
        extended.categoryDairy,
        extended.categoryMeat,
        extended.categoryBakery,
        extended.categoryDrinks,
        extended.categoryFrozen,
        extended.categoryHousehold,
        extended.categoryOther
    )
}

/** Neutral dot for products without a category (docs/05_Design_System.md §6.3). */
@Composable
fun uncategorizedDotColor(): Color =
    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
