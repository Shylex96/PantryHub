package com.pantryhub.core.designsystem.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * PantryHub color tokens.
 *
 * Two curated identities, one per mode (see docs/05_Design_System.md):
 *  - Dark  = "Nocturne" (Night Owl): deep navy canvas, lavender/violet accents, teal secondary.
 *  - Light = "Warm Pantry": sage green, earthy neutral and soft terracotta on warm off-white.
 *
 * Dynamic color (Material You) is OFF by default so this identity is preserved.
 */

// -----------------------------------------------------------------------------
// Dark — Nocturne (Night Owl)
// -----------------------------------------------------------------------------
val NocturnePrimary = Color(0xFFC792EA)
val NocturneOnPrimary = Color(0xFF24123A)
val NocturnePrimaryContainer = Color(0xFF4B3A7A)
val NocturneOnPrimaryContainer = Color(0xFFECDDFF)

val NocturneSecondary = Color(0xFF82AAFF)
val NocturneOnSecondary = Color(0xFF08224A)
val NocturneSecondaryContainer = Color(0xFF26406B)
val NocturneOnSecondaryContainer = Color(0xFFD8E4FF)

val NocturneTertiary = Color(0xFF21C7A8)
val NocturneOnTertiary = Color(0xFF00382D)
val NocturneTertiaryContainer = Color(0xFF005141)
val NocturneOnTertiaryContainer = Color(0xFFA9F0E0)

val NocturneBackground = Color(0xFF011627)
val NocturneOnBackground = Color(0xFFD6DEEB)
val NocturneSurface = Color(0xFF011627)
val NocturneOnSurface = Color(0xFFD6DEEB)
val NocturneSurfaceVariant = Color(0xFF0E2A3F)
val NocturneOnSurfaceVariant = Color(0xFF8FA9C4)

val NocturneSurfaceContainerLowest = Color(0xFF00101D)
val NocturneSurfaceContainerLow = Color(0xFF06203A)
val NocturneSurfaceContainer = Color(0xFF0B2942)
val NocturneSurfaceContainerHigh = Color(0xFF103450)
val NocturneSurfaceContainerHighest = Color(0xFF16405C)

val NocturneOutline = Color(0xFF3C5A74)
val NocturneOutlineVariant = Color(0xFF1E3648)
val NocturneError = Color(0xFFFF6B81)
val NocturneOnError = Color(0xFF400010)
val NocturneErrorContainer = Color(0xFF7A1F2E)
val NocturneOnErrorContainer = Color(0xFFFFD9DE)
val NocturneInverseSurface = Color(0xFFD6DEEB)
val NocturneInverseOnSurface = Color(0xFF0B2942)
val NocturneInversePrimary = Color(0xFF6A4BB0)

// -----------------------------------------------------------------------------
// Light — Warm Pantry (Sage / Earth / Terracotta)
// -----------------------------------------------------------------------------
val WarmPrimary = Color(0xFF4C665C)
val WarmOnPrimary = Color(0xFFFFFFFF)
val WarmPrimaryContainer = Color(0xFFD6E4DA)
val WarmOnPrimaryContainer = Color(0xFF0A1F17)

val WarmSecondary = Color(0xFF625B51)
val WarmOnSecondary = Color(0xFFFFFFFF)
val WarmSecondaryContainer = Color(0xFFEBE3D5)
val WarmOnSecondaryContainer = Color(0xFF201B14)

val WarmTertiary = Color(0xFF94544A)
val WarmOnTertiary = Color(0xFFFFFFFF)
val WarmTertiaryContainer = Color(0xFFF7D6D0)
val WarmOnTertiaryContainer = Color(0xFF3B0F09)

val WarmBackground = Color(0xFFFDFBFA)
val WarmOnBackground = Color(0xFF1C1B1A)
val WarmSurface = Color(0xFFFDFBFA)
val WarmOnSurface = Color(0xFF1C1B1A)
val WarmSurfaceVariant = Color(0xFFF0EAE4)
val WarmOnSurfaceVariant = Color(0xFF55504A)

val WarmSurfaceContainerLowest = Color(0xFFFFFFFF)
val WarmSurfaceContainerLow = Color(0xFFF8F3F0)
val WarmSurfaceContainer = Color(0xFFF4F0EF)
val WarmSurfaceContainerHigh = Color(0xFFEFE9E4)
val WarmSurfaceContainerHighest = Color(0xFFE9E3DD)

val WarmOutline = Color(0xFF857F78)
val WarmOutlineVariant = Color(0xFFD8D0C8)
val WarmError = Color(0xFFB3261E)
val WarmOnError = Color(0xFFFFFFFF)
val WarmErrorContainer = Color(0xFFF9DEDC)
val WarmOnErrorContainer = Color(0xFF410E0B)
val WarmInverseSurface = Color(0xFF313030)
val WarmInverseOnSurface = Color(0xFFF4F0EF)
val WarmInversePrimary = Color(0xFFA6BCB1)

// -----------------------------------------------------------------------------
// Extended colors (outside the M3 ColorScheme) — see PantryExtendedColors
// -----------------------------------------------------------------------------
// Favorites / status
val FavoriteDark = Color(0xFFECC48D)
val FavoriteLight = Color(0xFFB7791F)
// Ink for content drawn on top of a favorite (amber) fill; dark reads on both amber tones.
val OnFavorite = Color(0xFF2A1C00)
val SuccessDark = Color(0xFFADDB67)
val SuccessLight = Color(0xFF2E7D32)
val WarningDark = Color(0xFFF5B454)
val WarningLight = Color(0xFFB26A00)
val OnStatusDark = Color(0xFF04121F)
val OnStatusLight = Color(0xFFFFFFFF)

// Category colors (dark / light)
val CategoryVegetablesDark = Color(0xFFADDB67)
val CategoryVegetablesLight = Color(0xFF57A773)
val CategoryFruitDark = Color(0xFFF7B267)
val CategoryFruitLight = Color(0xFFE08A00)
val CategoryDairyDark = Color(0xFF82AAFF)
val CategoryDairyLight = Color(0xFF4C7DD9)
val CategoryMeatDark = Color(0xFFFF9E80)
val CategoryMeatLight = Color(0xFFC0562F)
val CategoryBakeryDark = Color(0xFFECC48D)
val CategoryBakeryLight = Color(0xFFB98A3E)
val CategoryDrinksDark = Color(0xFF21C7A8)
val CategoryDrinksLight = Color(0xFF0E8F79)
val CategoryFrozenDark = Color(0xFF86CBED)
val CategoryFrozenLight = Color(0xFF3E86A0)
val CategoryHouseholdDark = Color(0xFFC792EA)
val CategoryHouseholdLight = Color(0xFF8A5CC0)
val CategoryOtherDark = Color(0xFF8FA9C4)
val CategoryOtherLight = Color(0xFF6B7A8A)
