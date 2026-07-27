package com.pantryhub.core.designsystem.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme

@Composable
fun PantryLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
private fun PantryLoadingPreview() {
    PantryHubTheme {
        PantryLoading()
    }
}
