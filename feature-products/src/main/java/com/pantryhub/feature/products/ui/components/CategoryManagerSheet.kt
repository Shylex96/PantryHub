package com.pantryhub.feature.products.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryDialog
import com.pantryhub.core.designsystem.ui.components.PantryItemCard
import com.pantryhub.core.designsystem.ui.components.PantrySheet
import com.pantryhub.core.designsystem.ui.components.PantrySwipeRow
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.model.category.Category

/**
 * Create / rename / delete categories. Rename: tap the pencil to load the category into
 * the field, then confirm with the check. Delete: swipe left, then confirm (products in
 * that category become uncategorised).
 */
@Composable
fun CategoryManagerSheet(
    categories: List<Category>,
    onCreate: (String) -> Unit,
    onRename: (String, String) -> Unit,
    onDelete: (Category) -> Unit,
    onDismiss: () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    var input by remember { mutableStateOf("") }
    var editingId by remember { mutableStateOf<String?>(null) }
    var pendingDelete by remember { mutableStateOf<Category?>(null) }

    val toDelete = pendingDelete
    if (toDelete != null) {
        PantryDialog(
            onDismissRequest = { pendingDelete = null },
            title = stringResource(R.string.delete_category_confirm_title),
            confirmButton = {
                TextButton(onClick = {
                    onDelete(toDelete)
                    pendingDelete = null
                }) {
                    Text(stringResource(R.string.delete_confirm_action))
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) {
                    Text(stringResource(R.string.cancel_action))
                }
            }
        ) {
            Text(stringResource(R.string.delete_category_confirm_message, toDelete.name))
        }
    }

    PantrySheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.category_manager_title),
        subtitle = stringResource(R.string.sheet_categories_subtitle)
    ) {
        // Input row: placeholder field + solid square action (add, or confirm a rename).
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.sm)
        ) {
            PantryTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = stringResource(
                    if (editingId != null) R.string.rename_category_placeholder
                    else R.string.category_name_placeholder
                ),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            FilledIconButton(
                onClick = {
                    val name = input.trim()
                    if (name.isNotEmpty()) {
                        val id = editingId
                        if (id != null) onRename(id, name) else onCreate(name)
                        input = ""
                        editingId = null
                    }
                },
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(PantryHubTheme.radius.row),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = if (editingId != null) PantryIcons.Check else PantryIcons.Add,
                    contentDescription = stringResource(
                        if (editingId != null) R.string.rename_action else R.string.add_category_action
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(spacing.lg))

        if (categories.isEmpty()) {
            Text(
                text = stringResource(R.string.category_empty),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                categories.forEach { category ->
                    val isEditing = editingId == category.id
                    PantrySwipeRow(
                        onDelete = { pendingDelete = category },
                        dismissOnDelete = false
                    ) {
                        PantryItemCard(
                            containerColor = if (isEditing) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerHigh
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(categoryColor(category, categories), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(spacing.md))
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = if (isEditing) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    if (isEditing) {
                                        input = ""
                                        editingId = null
                                    } else {
                                        input = category.name
                                        editingId = category.id
                                    }
                                },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = if (isEditing) PantryIcons.Close else PantryIcons.Edit,
                                    contentDescription = stringResource(
                                        if (isEditing) R.string.cancel_action else R.string.rename_action
                                    ),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(spacing.sm))
            Text(
                text = stringResource(R.string.swipe_to_delete_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(horizontal = spacing.xs)
            )
        }
    }
}
