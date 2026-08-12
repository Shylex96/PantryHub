package com.pantryhub.feature.shopping.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.ui.components.PantryCheckbox
import com.pantryhub.core.designsystem.ui.components.PantryItemCard
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.model.shopping.ShoppingListItem

@Composable
fun ShoppingItemRow(
    item: ShoppingListItem,
    onToggle: (String) -> Unit,
    dotColor: Color,
    modifier: Modifier = Modifier
) {
    val spacing = PantryHubTheme.spacing
    val completed = item.isCompleted

    PantryItemCard(
        modifier = modifier.padding(vertical = spacing.xs),
        onClick = { onToggle(item.id) }
    ) {
        PantryCheckbox(
            checked = completed,
            onCheckedChange = { onToggle(item.id) }
        )
        Spacer(modifier = Modifier.width(spacing.md))
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Spacer(modifier = Modifier.width(spacing.sm))
        Text(
            text = item.product.name,
            style = MaterialTheme.typography.titleMedium,
            // Completed items read as "done": struck through and dimmed.
            color = if (completed) {
                MaterialTheme.colorScheme.onSurfaceVariant
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            textDecoration = if (completed) TextDecoration.LineThrough else null,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}
