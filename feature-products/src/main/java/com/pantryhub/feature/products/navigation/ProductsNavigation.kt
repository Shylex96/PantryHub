package com.pantryhub.feature.products.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pantryhub.core.navigation.Destination
import com.pantryhub.feature.products.presentation.ProductsViewModel
import com.pantryhub.feature.products.ui.ProductsScreen

fun NavGraphBuilder.productsGraph() {
    composable<Destination.Products> {
        val viewModel: ProductsViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsState()
        
        ProductsScreen(
            state = state,
            onIntent = viewModel::handleIntent
        )
    }
}
