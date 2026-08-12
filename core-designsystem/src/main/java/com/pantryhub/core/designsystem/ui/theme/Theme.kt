package com.pantryhub.core.designsystem.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// -----------------------------------------------------------------------------
// Extended colors (favorites, status, categories) — not part of the M3 scheme.
// -----------------------------------------------------------------------------
data class PantryExtendedColors(
    val favorite: Color,
    val onFavorite: Color,
    val success: Color,
    val warning: Color,
    val onStatus: Color,
    val categoryVegetables: Color,
    val categoryFruit: Color,
    val categoryDairy: Color,
    val categoryMeat: Color,
    val categoryBakery: Color,
    val categoryDrinks: Color,
    val categoryFrozen: Color,
    val categoryHousehold: Color,
    val categoryOther: Color
)

private val DarkExtendedColors = PantryExtendedColors(
    favorite = FavoriteDark,
    onFavorite = OnFavorite,
    success = SuccessDark,
    warning = WarningDark,
    onStatus = OnStatusDark,
    categoryVegetables = CategoryVegetablesDark,
    categoryFruit = CategoryFruitDark,
    categoryDairy = CategoryDairyDark,
    categoryMeat = CategoryMeatDark,
    categoryBakery = CategoryBakeryDark,
    categoryDrinks = CategoryDrinksDark,
    categoryFrozen = CategoryFrozenDark,
    categoryHousehold = CategoryHouseholdDark,
    categoryOther = CategoryOtherDark
)

private val LightExtendedColors = PantryExtendedColors(
    favorite = FavoriteLight,
    onFavorite = OnFavorite,
    success = SuccessLight,
    warning = WarningLight,
    onStatus = OnStatusLight,
    categoryVegetables = CategoryVegetablesLight,
    categoryFruit = CategoryFruitLight,
    categoryDairy = CategoryDairyLight,
    categoryMeat = CategoryMeatLight,
    categoryBakery = CategoryBakeryLight,
    categoryDrinks = CategoryDrinksLight,
    categoryFrozen = CategoryFrozenLight,
    categoryHousehold = CategoryHouseholdLight,
    categoryOther = CategoryOtherLight
)

val LocalPantryExtendedColors = staticCompositionLocalOf { LightExtendedColors }

// -----------------------------------------------------------------------------
// Material 3 color schemes.
// Dark = Nocturne (Night Owl) · Light = Warm Pantry.
// -----------------------------------------------------------------------------
private val DarkColorScheme = darkColorScheme(
    primary = NocturnePrimary,
    onPrimary = NocturneOnPrimary,
    primaryContainer = NocturnePrimaryContainer,
    onPrimaryContainer = NocturneOnPrimaryContainer,
    secondary = NocturneSecondary,
    onSecondary = NocturneOnSecondary,
    secondaryContainer = NocturneSecondaryContainer,
    onSecondaryContainer = NocturneOnSecondaryContainer,
    tertiary = NocturneTertiary,
    onTertiary = NocturneOnTertiary,
    tertiaryContainer = NocturneTertiaryContainer,
    onTertiaryContainer = NocturneOnTertiaryContainer,
    background = NocturneBackground,
    onBackground = NocturneOnBackground,
    surface = NocturneSurface,
    onSurface = NocturneOnSurface,
    surfaceVariant = NocturneSurfaceVariant,
    onSurfaceVariant = NocturneOnSurfaceVariant,
    surfaceContainerLowest = NocturneSurfaceContainerLowest,
    surfaceContainerLow = NocturneSurfaceContainerLow,
    surfaceContainer = NocturneSurfaceContainer,
    surfaceContainerHigh = NocturneSurfaceContainerHigh,
    surfaceContainerHighest = NocturneSurfaceContainerHighest,
    outline = NocturneOutline,
    outlineVariant = NocturneOutlineVariant,
    error = NocturneError,
    onError = NocturneOnError,
    errorContainer = NocturneErrorContainer,
    onErrorContainer = NocturneOnErrorContainer,
    inverseSurface = NocturneInverseSurface,
    inverseOnSurface = NocturneInverseOnSurface,
    inversePrimary = NocturneInversePrimary,
    surfaceTint = NocturnePrimary
)

private val LightColorScheme = lightColorScheme(
    primary = WarmPrimary,
    onPrimary = WarmOnPrimary,
    primaryContainer = WarmPrimaryContainer,
    onPrimaryContainer = WarmOnPrimaryContainer,
    secondary = WarmSecondary,
    onSecondary = WarmOnSecondary,
    secondaryContainer = WarmSecondaryContainer,
    onSecondaryContainer = WarmOnSecondaryContainer,
    tertiary = WarmTertiary,
    onTertiary = WarmOnTertiary,
    tertiaryContainer = WarmTertiaryContainer,
    onTertiaryContainer = WarmOnTertiaryContainer,
    background = WarmBackground,
    onBackground = WarmOnBackground,
    surface = WarmSurface,
    onSurface = WarmOnSurface,
    surfaceVariant = WarmSurfaceVariant,
    onSurfaceVariant = WarmOnSurfaceVariant,
    surfaceContainerLowest = WarmSurfaceContainerLowest,
    surfaceContainerLow = WarmSurfaceContainerLow,
    surfaceContainer = WarmSurfaceContainer,
    surfaceContainerHigh = WarmSurfaceContainerHigh,
    surfaceContainerHighest = WarmSurfaceContainerHighest,
    outline = WarmOutline,
    outlineVariant = WarmOutlineVariant,
    error = WarmError,
    onError = WarmOnError,
    errorContainer = WarmErrorContainer,
    onErrorContainer = WarmOnErrorContainer,
    inverseSurface = WarmInverseSurface,
    inverseOnSurface = WarmInverseOnSurface,
    inversePrimary = WarmInversePrimary,
    surfaceTint = WarmPrimary
)

@Composable
fun PantryHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color (Material You) is OFF by default to preserve the PantryHub identity.
    // It can be enabled from Settings later; only takes effect on Android 12+.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(
        LocalPantrySpacing provides PantrySpacing(),
        LocalPantryIconSize provides PantryIconSize(),
        LocalPantryElevation provides PantryElevation(),
        LocalPantryTouchTarget provides PantryTouchTarget(),
        LocalPantryExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = PantryShapes,
            content = content
        )
    }
}

object PantryHubTheme {
    val spacing: PantrySpacing
        @Composable
        get() = LocalPantrySpacing.current

    val icons: PantryIconSize
        @Composable
        get() = LocalPantryIconSize.current

    val elevations: PantryElevation
        @Composable
        get() = LocalPantryElevation.current

    val touchTargets: PantryTouchTarget
        @Composable
        get() = LocalPantryTouchTarget.current

    val extendedColors: PantryExtendedColors
        @Composable
        get() = LocalPantryExtendedColors.current

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes
}
