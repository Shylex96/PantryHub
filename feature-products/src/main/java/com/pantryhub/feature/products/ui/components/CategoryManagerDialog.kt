package com.pantryhub.feature.products.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.model.category.Category

/**
 * Create / rename / delete categories. Rename is triggered by tapping the edit
 * icon on a row, which loads that category into the input field.
 */
@Composable
fun CategoryManagerDialog(
    categories: List<Category>,
    categoryColor: (String) -> Color,
    onCreate: (String) -> Unit,
    onRename: (String, String) -> Unit,
    onDelete: (Category) -> Unit,
    onDismiss: () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    var input by remember { mutableStateOf("") }
    var editingId by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.category_manager_title)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PantryTextField(
                        value = input,
                        onValueChange = { input = it },
                        label = stringResource(R.string.category_name_placeholder),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(spacing.sm))
                    IconButton(onClick = {
                        val name = input.trim()
                        if (name.isNotEmpty()) {
                            val id = editingId
                            if (id != null) onRename(id, name) else onCreate(name)
                            input = ""
                            editingId = null
                        }
                    }) {
                        Icon(
                            imageVector = if (editingId != null) PantryIcons.Check else PantryIcons.Add,
                            contentDescription = null
                        )
                    }
                }

                Spacer(modifier = Modifier.height(spacing.md))

                if (categories.isEmpty()) {
                    Text(
                        text = stringResource(R.string.category_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    categories.forEach { category ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = spacing.xs)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(categoryColor(category.id), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(spacing.sm))
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    input = category.name
                                    editingId = category.id
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = PantryIcons.Edit,
                                    contentDescription = stringResource(R.string.rename_action),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(
                                onClick = { onDelete(category) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = PantryIcons.Delete,
                                    contentDescription = stringResource(R.string.delete_action),
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.done_action))
            }
        }
    )
}
