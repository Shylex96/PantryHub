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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.navigation.Destination
import com.pantryhub.core.navigation.NavigationActions

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
                    NavigationItem("Lists", Icons.AutoMirrored.Filled.List, Destination.ShoppingLists),
                    NavigationItem("Products", Icons.Default.ShoppingCart, Destination.Products),
                    NavigationItem("Notes", Icons.Default.Star, Destination.Notes),
                    NavigationItem("Settings", Icons.Default.Settings, Destination.Settings),
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
                composable<Destination.ShoppingLists> {
                    PlaceholderScreen("Shopping Lists")
                }
                composable<Destination.Products> {
                    PlaceholderScreen("Products catalog")
                }
                composable<Destination.Notes> {
                    PlaceholderScreen("Quick notes")
                }
                composable<Destination.Settings> {
                    PlaceholderScreen("Settings")
                }
                composable<Destination.ShoppingMode> {
                    PlaceholderScreen("Shopping Mode")
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Text(text = "Screen: $name")
}

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val destination: Destination
)
