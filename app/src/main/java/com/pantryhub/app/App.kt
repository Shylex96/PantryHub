package com.pantryhub.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.navigation.Destination
import com.pantryhub.core.navigation.NavigationActions
import com.pantryhub.feature.shopping.navigation.shoppingGraph

@Composable
fun PantryHubApp() {
    val navController = rememberNavController()
    val navActions = remember(navController) { NavigationActions(navController) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    PantryHubTheme {
        Scaffold(
            bottomBar = {
                val items = listOf(
                    NavigationItem(
                        label = stringResource(R.string.nav_lists), 
                        icon = Icons.AutoMirrored.Filled.List, 
                        destination = Destination.ShoppingLists
                    ),
                    NavigationItem(
                        label = stringResource(R.string.nav_products), 
                        icon = Icons.Default.ShoppingCart, 
                        destination = Destination.Products
                    ),
                    NavigationItem(
                        label = stringResource(R.string.nav_notes), 
                        icon = Icons.Default.Star, 
                        destination = Destination.Notes
                    ),
                    NavigationItem(
                        label = stringResource(R.string.nav_settings), 
                        icon = Icons.Default.Settings, 
                        destination = Destination.Settings
                    ),
                )

                NavigationBar {
                    items.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(item.destination::class)
                        } == true

                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
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
                
                composable<Destination.Products> {
                    PlaceholderScreen(stringResource(R.string.nav_products))
                }
                composable<Destination.Notes> {
                    PlaceholderScreen(stringResource(R.string.nav_notes))
                }
                composable<Destination.Settings> {
                    PlaceholderScreen(stringResource(R.string.nav_settings))
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Text(
        text = name,
        modifier = Modifier.padding(PantryHubTheme.spacing.lg),
        style = PantryHubTheme.typography.headlineMedium
    )
}

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val destination: Destination
)
