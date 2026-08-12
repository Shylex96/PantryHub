package com.pantryhub.feature.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pantryhub.core.navigation.Destination
import com.pantryhub.feature.settings.ui.HelpScreen
import com.pantryhub.feature.settings.ui.SettingsScreen

fun NavGraphBuilder.settingsGraph(
    onOpenImportExport: () -> Unit,
    onOpenHelp: () -> Unit,
    onBack: () -> Unit
) {
    composable<Destination.Settings> {
        SettingsScreen(
            onOpenImportExport = onOpenImportExport,
            onOpenHelp = onOpenHelp
        )
    }
    composable<Destination.Help> {
        HelpScreen(onBack = onBack)
    }
}
