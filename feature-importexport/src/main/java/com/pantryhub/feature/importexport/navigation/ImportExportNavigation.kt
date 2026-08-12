package com.pantryhub.feature.importexport.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pantryhub.core.navigation.Destination
import com.pantryhub.feature.importexport.ui.ImportExportScreen

/** Import/Export is reached from the Settings screen. */
fun NavGraphBuilder.importExportGraph(onBack: () -> Unit) {
    composable<Destination.ImportExport> {
        ImportExportScreen(onBack = onBack)
    }
}
