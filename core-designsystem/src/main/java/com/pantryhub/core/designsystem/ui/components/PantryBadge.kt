package com.pantryhub.core.designsystem.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme

/**
 * A small pill-shaped badge used for short status/type labels (list types,
 * categories, etc.). Optionally shows a leading color dot.
 */
@Composable
fun PantryBadge(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    dotColor: Color? = null
) {
    val spacing = PantryHubTheme.spacing
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(containerColor)
            .padding(horizontal = spacing.sm, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (dotColor != null) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Spacer(modifier = Modifier.width(spacing.xs))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PantryBadgePreview() {
    PantryHubTheme {
        Row(modifier = Modifier.padding(8.dp)) {
            PantryBadge(text = "One-off")
            Spacer(modifier = Modifier.width(8.dp))
            PantryBadge(
                text = "Dairy",
                dotColor = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}
