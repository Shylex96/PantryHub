package com.pantryhub.core.designsystem.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme

/**
 * Reusable list item for products, lists, or notes.
 */
@Composable
fun PantryListItem(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val spacing = PantryHubTheme.spacing
    val clickableModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(clickableModifier)
            .padding(spacing.lg),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingContent != null) {
            leadingContent()
            Spacer(modifier = Modifier.width(spacing.lg))
        }
        
        Row(modifier = Modifier.weight(1f)) {
            Row {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.width(spacing.sm))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        if (trailingContent != null) {
            Spacer(modifier = Modifier.width(spacing.lg))
            trailingContent()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PantryListItemPreview() {
    PantryHubTheme {
        PantryListItem(
            title = "Item Title",
            subtitle = "Subtitle detail",
            leadingContent = {
                Icon(Icons.Default.Info, contentDescription = null)
            },
            onClick = {}
        )
    }
}
