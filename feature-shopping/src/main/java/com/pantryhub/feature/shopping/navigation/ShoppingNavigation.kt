package com.pantryhub.feature.shopping.navigation

import androidx.compose.runtime.LaunchedEffect
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
                navController.navigate(Destination.ShoppingListDetail(id))
            },
            onDeleteList = { id ->
                viewModel.handleIntent(ShoppingIntent.DeleteList(id))
            },
            onCreateList = { name ->
                viewModel.handleIntent(ShoppingIntent.CreateList(name))
            }
        )
    }

    composable<Destination.ShoppingListDetail> { backStackEntry ->
        val route: Destination.ShoppingListDetail = backStackEntry.toRoute()
        val viewModel: ShoppingViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsState()

        LaunchedEffect(route.listId) {
            viewModel.handleIntent(ShoppingIntent.OpenList(route.listId))
        }

        ShoppingListDetailScreen(
            state = state,
            onAddItem = { name, qty ->
                viewModel.handleIntent(ShoppingIntent.AddItem(name, qty))
            },
            onDeleteItem = { id ->
                viewModel.handleIntent(ShoppingIntent.DeleteItem(id))
            },
            onDeleteList = { id ->
                viewModel.handleIntent(ShoppingIntent.DeleteList(id))
            },
            onRenameList = { newName ->
                viewModel.handleIntent(ShoppingIntent.RenameList(newName))
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

        LaunchedEffect(route.listId) {
            viewModel.handleIntent(ShoppingIntent.OpenList(route.listId))
        }

        ShoppingModeScreen(
            state = state,
            onToggleItem = { itemId ->
                viewModel.handleIntent(ShoppingIntent.ToggleItem(itemId))
            },
            onFinishShopping = { supermarket, price ->
                viewModel.handleIntent(ShoppingIntent.FinishWithData(supermarket, price))
            },
            onBack = {
                navController.popBackStack()
            }
        )
    }
}
