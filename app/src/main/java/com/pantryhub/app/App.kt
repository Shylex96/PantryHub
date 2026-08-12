package com.pantryhub.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.navigation.Destination
import com.pantryhub.core.navigation.NavigationActions
import com.pantryhub.core.model.settings.ThemeMode
import com.pantryhub.feature.importexport.navigation.importExportGraph
import com.pantryhub.feature.notes.navigation.notesGraph
import com.pantryhub.feature.products.navigation.productsGraph
import com.pantryhub.feature.settings.navigation.settingsGraph
import com.pantryhub.feature.shopping.navigation.shoppingGraph

@Composable
fun PantryHubApp() {
    val appViewModel: AppViewModel = hiltViewModel()
    val settings by appViewModel.settings.collectAsState()
    val darkTheme = when (settings.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val navController = rememberNavController()
    val navActions = remember(navController) { NavigationActions(navController) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    PantryHubTheme(darkTheme = darkTheme, dynamicColor = settings.dynamicColor) {
        Scaffold(
            bottomBar = {
                val items = listOf(
                    NavigationItem(
                        label = stringResource(R.string.nav_lists), 
                        icon = PantryIcons.Lists, 
                        destination = Destination.ShoppingLists
                    ),
                    NavigationItem(
                        label = stringResource(R.string.nav_products), 
                        icon = PantryIcons.Products, 
                        destination = Destination.Products
                    ),
                    NavigationItem(
                        label = stringResource(R.string.nav_notes), 
                        icon = PantryIcons.Notes, 
                        destination = Destination.Notes
                    ),
                    NavigationItem(
                        label = stringResource(R.string.nav_settings), 
                        icon = PantryIcons.Settings, 
                        destination = Destination.Settings
                    ),
                )

                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = PantryHubTheme.elevations.low
                ) {
                    items.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(item.destination::class)
                        } == true

                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            selected = selected,
                            onClick = { navActions.navigateTo(item.destination) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Destination.ShoppingLists,
                modifier = Modifier.padding(innerPadding)
            ) {
                shoppingGraph(navController)
                productsGraph()
                
                notesGraph()
                settingsGraph(
                    onOpenImportExport = { navController.navigate(Destination.ImportExport) }
                )
                importExportGraph(onBack = { navController.popBackStack() })
            }
        }
    }
}

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val destination: Destination
)
