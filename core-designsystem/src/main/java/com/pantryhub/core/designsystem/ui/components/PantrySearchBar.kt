package com.pantryhub.core.designsystem.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme

@Composable
fun PantrySearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    PantryTextField(
        value = query,
        onValueChange = onQueryChange,
        label = placeholder,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = PantryHubTheme.spacing.sm),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}
