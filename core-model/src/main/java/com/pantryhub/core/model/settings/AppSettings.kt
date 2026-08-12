package com.pantryhub.core.model.settings

data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    // Material You dynamic color; off by default to preserve the PantryHub identity.
    val dynamicColor: Boolean = false
)
