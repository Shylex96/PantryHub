package com.pantryhub.core.designsystem.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme

/**
 * The single dialog container for PantryHub. Wraps [AlertDialog] so every dialog
 * shares the same shape, surface color, title weight and button placement — polish
 * a dialog here once instead of on each screen.
 */
@Composable
fun PantryDialog(
    onDismissRequest: () -> Unit,
    title: String,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        shape = PantryHubTheme.shapes.large,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = content,
        confirmButton = confirmButton,
        dismissButton = dismissButton
    )
}
