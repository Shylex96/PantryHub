package com.pantryhub.core.designsystem.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme

/**
 * A filled, rounded row container used for list items (shopping list detail and
 * shopping mode). Unlike [PantryCard] it has a solid, clearly visible fill
 * (`surfaceContainerHigh`) rather than a subtle bordered surface, matching the
 * product design. Content is laid out in a centered [Row].
 */
@Composable
fun PantryItemCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    val spacing = PantryHubTheme.spacing
    val clickable = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(PantryHubTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .then(clickable)
            .padding(horizontal = spacing.lg, vertical = spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}
