package com.pantryhub.core.designsystem.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme

/**
 * Standardized card for the PantryHub design system.
 */
@Composable
fun PantryCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = PantryHubTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = PantryHubTheme.elevations.low
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        ),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
private fun PantryCardPreview() {
    PantryHubTheme {
        PantryCard(
            modifier = Modifier.padding(PantryHubTheme.spacing.md)
        ) {
            Column(modifier = Modifier.padding(PantryHubTheme.spacing.lg)) {
                Text(
                    text = "Card Title",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "This is a sample card content in the PantryHub design system.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
