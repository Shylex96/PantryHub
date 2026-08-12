package com.pantryhub.feature.importexport.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.domain.backup.ImportPreview

/**
 * Shows what an import will do (counts) and lets the user resolve each ambiguous
 * product: treat it as the existing one ("Same") or import it as new ("New").
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportPreviewDialog(
    preview: ImportPreview,
    decisions: Map<String, Boolean>,
    onDecisionChange: (String, Boolean) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.import_preview_title)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    text = stringResource(
                        R.string.import_preview_summary,
                        preview.newProductCount,
                        preview.autoMergeCount,
                        preview.listCount,
                        preview.categoryCount,
                        preview.noteCount
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (preview.conflicts.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(spacing.lg))
                    Text(
                        text = stringResource(R.string.import_conflicts_title),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(spacing.sm))

                    preview.conflicts.forEach { conflict ->
                        val treatAsSame = decisions[conflict.importedProductId] == true
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = spacing.sm)
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.import_conflict_desc,
                                    conflict.importedName,
                                    conflict.existingName,
                                    conflict.similarityPercent
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(spacing.xs))
                            Row(horizontalArrangement = Arrangement.spacedBy(spacing.sm)) {
                                FilterChip(
                                    selected = !treatAsSame,
                                    onClick = { onDecisionChange(conflict.importedProductId, false) },
                                    label = { Text(stringResource(R.string.conflict_new)) }
                                )
                                FilterChip(
                                    selected = treatAsSame,
                                    onClick = { onDecisionChange(conflict.importedProductId, true) },
                                    label = { Text(stringResource(R.string.conflict_same)) }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.import_confirm_action))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_action))
            }
        }
    )
}
