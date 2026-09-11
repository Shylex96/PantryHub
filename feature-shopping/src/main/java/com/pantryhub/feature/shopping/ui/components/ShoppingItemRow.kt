package com.pantryhub.feature.shopping.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryCheckbox
import com.pantryhub.core.designsystem.ui.components.PantryItemCard
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.model.shopping.ShoppingListItem

/**
 * Shopping-mode row (docs/05_Design_System.md §6.3): rounded checkbox, category dot,
 * product name and the quantity when it is not 1. A completed row uses the quieter
 * `surfaceContainerLow` fill, a dimmed dot and struck-through text.
 */
@Composable
fun ShoppingItemRow(
    item: ShoppingListItem,
    onToggle: (String) -> Unit,
    dotColor: Color,
    modifier: Modifier = Modifier
) {
    val spacing = PantryHubTheme.spacing
    val completed = item.isCompleted
    val container = if (completed) {
        MaterialTheme.colorScheme.surfaceContainerLow
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }

    PantryItemCard(
        modifier = modifier,
        onClick = { onToggle(item.id) },
        containerColor = container
    ) {
        PantryCheckbox(
            checked = completed,
            onCheckedChange = { onToggle(item.id) }
        )
        Spacer(modifier = Modifier.width(14.dp))
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (completed) dotColor.copy(alpha = 0.5f) else dotColor)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = item.product.name,
            style = if (completed) {
                MaterialTheme.typography.bodyMedium
            } else {
                MaterialTheme.typography.bodyLarge
            },
            fontWeight = if (completed) FontWeight.Normal else FontWeight.Medium,
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
        if (!completed && item.quantity != 1.0) {
            Spacer(modifier = Modifier.width(spacing.sm))
            Text(
                text = stringResource(R.string.quantity_times, formatQuantity(item.quantity)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** "2" for whole quantities, "1.5" otherwise — no trailing ".0". */
internal fun formatQuantity(quantity: Double): String =
    if (quantity % 1.0 == 0.0) quantity.toLong().toString() else quantity.toString()
