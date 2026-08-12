package com.pantryhub.feature.notes.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pantryhub.core.navigation.Destination
import com.pantryhub.feature.notes.ui.NotesScreen

fun NavGraphBuilder.notesGraph() {
    composable<Destination.Notes> {
        NotesScreen()
    }
}
