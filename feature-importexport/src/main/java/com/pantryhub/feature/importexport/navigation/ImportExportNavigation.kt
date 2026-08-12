package com.pantryhub.feature.importexport.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pantryhub.core.navigation.Destination
import com.pantryhub.feature.importexport.ui.ImportExportScreen

/**
 * Temporarily occupies the Settings tab until a full Settings screen exists
 * (Sprint 5), at which point Import/Export becomes a section within it.
 */
fun NavGraphBuilder.importExportGraph() {
    composable<Destination.Settings> {
        ImportExportScreen()
    }
}
