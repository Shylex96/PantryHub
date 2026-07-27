package com.pantryhub.core.designsystem.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme

/**
 * Consistent top bar for all screens in PantryHub.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantryTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { 
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            ) 
        },
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Preview
@Composable
private fun PantryTopBarPreview() {
    PantryHubTheme {
        PantryTopBar(
            title = "Screen Title",
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            }
        )
    }
}
