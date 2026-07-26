package com.pantryhub.feature.shopping.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pantryhub.core.navigation.Destination
import com.pantryhub.feature.shopping.presentation.ShoppingIntent
import com.pantryhub.feature.shopping.presentation.ShoppingViewModel
import com.pantryhub.feature.shopping.ui.screens.ShoppingListDetailScreen
import com.pantryhub.feature.shopping.ui.screens.ShoppingListsScreen
import com.pantryhub.feature.shopping.ui.screens.ShoppingModeScreen

fun NavGraphBuilder.shoppingGraph(
    navController: NavController
) {
    composable<Destination.ShoppingLists> {
        val viewModel: ShoppingViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsState()

        ShoppingListsScreen(
            state = state,
            onListClick = { id ->
                viewModel.handleIntent(ShoppingIntent.OpenList(id))
                navController.navigate(Destination.ShoppingListDetail(id))
            },
            onCreateList = {
                viewModel.handleIntent(ShoppingIntent.CreateList("New List"))
            }
        )
    }

    composable<Destination.ShoppingListDetail> { backStackEntry ->
        val route: Destination.ShoppingListDetail = backStackEntry.toRoute()
        val viewModel: ShoppingViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsState()

        viewModel.handleIntent(ShoppingIntent.OpenList(route.listId))

        ShoppingListDetailScreen(
            state = state,
            onAddItem = { name, qty ->
                viewModel.handleIntent(ShoppingIntent.AddItem(name, qty))
            },
            onDeleteItem = { id ->
                viewModel.handleIntent(ShoppingIntent.DeleteItem(id))
            },
            onStartShopping = {
                navController.navigate(Destination.ShoppingMode(route.listId))
            },
            onBack = {
                navController.popBackStack()
            }
        )
    }

    composable<Destination.ShoppingMode> { backStackEntry ->
        val route: Destination.ShoppingMode = backStackEntry.toRoute()
        val viewModel: ShoppingViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsState()

        // Ensure the correct list is loaded in the ViewModel for this screen
        viewModel.handleIntent(ShoppingIntent.OpenList(route.listId))

        ShoppingModeScreen(
            state = state,
            onToggleItem = { itemId ->
                viewModel.handleIntent(ShoppingIntent.ToggleItem(itemId))
            },
            onBack = {
                navController.popBackStack()
            }
        )
    }
}
