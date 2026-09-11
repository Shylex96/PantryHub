package com.pantryhub.feature.products.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryDialog
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.model.category.Category

/**
 * Edit an existing product: its aliases (comma-separated) and its category.
 * Changes apply on Save. Aliases feed alias-based search ("papa" -> "Patata").
 */
@Composable
fun EditProductDialog(
    productName: String,
    categories: List<Category>,
    initialCategoryId: String?,
    initialAliases: List<String>,
    categoryColor: (String) -> Color,
    onSave: (categoryId: String?, aliases: List<String>) -> Unit,
    onDismiss: () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    var selectedCategoryId by remember { mutableStateOf(initialCategoryId) }
    var aliasesInput by remember { mutableStateOf(initialAliases.joinToString(", ")) }

    PantryDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.edit_product_title, productName),
        confirmButton = {
            TextButton(onClick = {
                val aliases = aliasesInput
                    .split(",")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                onSave(selectedCategoryId, aliases)
            }) {
                Text(stringResource(R.string.save_action))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_action))
            }
        }
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            PantryTextField(
                value = aliasesInput,
                onValueChange = { aliasesInput = it },
                label = stringResource(R.string.aliases_label),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(spacing.lg))

            Text(
                text = stringResource(R.string.new_product_category_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(spacing.sm))

            CategoryOption(
                label = stringResource(R.string.category_none),
                dotColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                selected = selectedCategoryId == null,
                onClick = { selectedCategoryId = null }
            )
            categories.forEach { category ->
                CategoryOption(
                    label = category.name,
                    dotColor = categoryColor(category.id),
                    selected = selectedCategoryId == category.id,
                    onClick = { selectedCategoryId = category.id }
                )
            }
        }
    }
}

/** One selectable category row: color dot · name · check when selected. */
@Composable
private fun CategoryOption(
    label: String,
    dotColor: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacing.xs)
            .clip(PantryHubTheme.shapes.medium)
            .background(
                if (selected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceContainerHighest
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = spacing.md, vertical = spacing.md)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(dotColor, CircleShape)
        )
        Spacer(modifier = Modifier.width(spacing.md))
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = if (selected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        if (selected) {
            Icon(
                imageVector = PantryIcons.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
