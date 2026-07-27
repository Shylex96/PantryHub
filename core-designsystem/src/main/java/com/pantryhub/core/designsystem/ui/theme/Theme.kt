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
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Sage80,
    onPrimary = WarmGrey10,
    primaryContainer = Sage40,
    onPrimaryContainer = Sage90,
    secondary = Earth80,
    onSecondary = WarmGrey10,
    tertiary = Terracotta80,
    onTertiary = WarmGrey10,
    background = WarmGrey10,
    onBackground = WarmGrey95,
    surface = WarmGrey10,
    onSurface = WarmGrey95,
    error = ErrorRedDark
)

private val LightColorScheme = lightColorScheme(
    primary = Sage40,
    onPrimary = WarmGrey99,
    primaryContainer = Sage90,
    onPrimaryContainer = Sage40,
    secondary = Earth40,
    onSecondary = WarmGrey99,
    tertiary = Terracotta40,
    onTertiary = WarmGrey99,
    background = WarmGrey99,
    onBackground = WarmGrey10,
    surface = WarmGrey99,
    onSurface = WarmGrey10,
    error = ErrorRed
)

@Composable
fun PantryHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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

    CompositionLocalProvider(
        LocalPantrySpacing provides PantrySpacing(),
        LocalPantryIconSize provides PantryIconSize(),
        LocalPantryElevation provides PantryElevation(),
        LocalPantryTouchTarget provides PantryTouchTarget()
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

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes
}
