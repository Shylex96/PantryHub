package com.pantryhub.core.designsystem.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            .padding(horizontal = spacing.lg, vertical = spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingContent != null) {
            Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                leadingContent()
            }
            Spacer(modifier = Modifier.width(spacing.md))
        }
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        
        if (trailingContent != null) {
            Spacer(modifier = Modifier.width(spacing.md))
            Box(
                modifier = Modifier.align(Alignment.CenterVertically),
                contentAlignment = Alignment.Center
            ) {
                trailingContent()
            }
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
