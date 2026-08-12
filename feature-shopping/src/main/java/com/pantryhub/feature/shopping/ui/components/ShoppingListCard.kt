package com.pantryhub.feature.shopping.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryBadge
import com.pantryhub.core.designsystem.ui.components.PantryCard
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.model.shopping.ShoppingList
import com.pantryhub.core.model.shopping.ShoppingListType

@Composable
fun ShoppingListCard(
    shoppingList: ShoppingList,
    onListClick: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = PantryHubTheme.spacing
    val isTemporary = shoppingList.type == ShoppingListType.TEMPORARY

    PantryCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacing.xs)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onListClick(shoppingList.id) }
                .padding(spacing.lg),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Leading icon in a tinted circle for a stronger visual anchor.
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = PantryIcons.Lists,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(PantryHubTheme.icons.md)
                )
            }

            Spacer(modifier = Modifier.width(spacing.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = shoppingList.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(spacing.xs))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(
                            R.string.item_count_subtitle,
                            shoppingList.items.size
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(spacing.sm))
                    // Both types get a badge: one-off pops in the tertiary accent,
                    // regular stays subtle so it reads as the quiet default.
                    if (isTemporary) {
                        PantryBadge(
                            text = stringResource(R.string.list_badge_temporary),
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    } else {
                        PantryBadge(
                            text = stringResource(R.string.list_badge_regular),
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            IconButton(
                onClick = { onDelete(shoppingList.id) },
                modifier = Modifier.size(spacing.xl)
            ) {
                Icon(
                    imageVector = PantryIcons.Delete,
                    contentDescription = stringResource(R.string.delete_action),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
