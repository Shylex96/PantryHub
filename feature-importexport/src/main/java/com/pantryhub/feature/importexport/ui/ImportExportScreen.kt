package com.pantryhub.feature.importexport.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryButton
import com.pantryhub.core.designsystem.ui.components.PantryOutlinedButton
import com.pantryhub.core.designsystem.ui.components.PantryTopBar
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.domain.backup.ImportPreview
import com.pantryhub.feature.importexport.presentation.ImportExportViewModel
import com.pantryhub.feature.importexport.ui.components.ImportPreviewDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ImportExportScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: ImportExportViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val spacing = PantryHubTheme.spacing

    var busy by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    var preview by remember { mutableStateOf<ImportPreview?>(null) }
    // importedProductId -> treat as the same as the existing product (merge).
    var conflictDecisions by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }

    val exportedMessage = stringResource(R.string.export_success)
    val errorMessage = stringResource(R.string.io_error)

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            scope.launch {
                busy = true
                isError = false
                statusMessage = null
                try {
                    val json = viewModel.buildBackupJson()
                    withContext(Dispatchers.IO) {
                        // "wt" = write + truncate. Without truncation, overwriting a file
                        // that was previously larger leaves stale bytes at the end and
                        // corrupts the JSON.
                        context.contentResolver.openOutputStream(uri, "wt")?.use { output ->
                            output.write(json.toByteArray())
                        } ?: error("Could not open output stream")
                    }
                    statusMessage = exportedMessage
                } catch (e: Exception) {
                    isError = true
                    statusMessage = errorMessage
                    // Debug: uncomment these two lines to surface the real cause
                    // (exception + message) on screen and in logcat.
                    // android.util.Log.e("PantryHubImport", "Import step failed", e)
                    // statusMessage = "$errorMessage\n${e.javaClass.simpleName}: ${e.message}"
                } finally {
                    busy = false
                }
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                busy = true
                isError = false
                statusMessage = null
                try {
                    val text = withContext(Dispatchers.IO) {
                        context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                    } ?: error("Could not open input stream")
                    conflictDecisions = emptyMap()
                    preview = viewModel.analyzeImport(text)
                } catch (e: Exception) {
                    isError = true
                    statusMessage = errorMessage
                    // Debug: uncomment these two lines to surface the real cause
                    // (exception + message) on screen and in logcat.
                    // android.util.Log.e("PantryHubImport", "Import step failed", e)
                    // statusMessage = "$errorMessage\n${e.javaClass.simpleName}: ${e.message}"
                } finally {
                    busy = false
                }
            }
        }
    }

    Scaffold(
        topBar = {
            PantryTopBar(
                title = stringResource(R.string.backup_section_title),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = PantryIcons.Back,
                            contentDescription = stringResource(R.string.back_description)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = spacing.lg, vertical = spacing.md),
            verticalArrangement = Arrangement.spacedBy(spacing.md)
        ) {
            Text(
                text = stringResource(R.string.backup_section_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            PantryButton(
                onClick = { exportLauncher.launch("pantryhub-backup.json") },
                enabled = !busy,
                isLoading = busy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.export_action))
            }

            PantryOutlinedButton(
                // Accept any file type: some file managers (e.g. MIUI) report .json
                // as octet-stream / text, which a strict json filter would grey out.
                onClick = { importLauncher.launch(arrayOf("*/*")) },
                enabled = !busy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.import_action))
            }

            statusMessage?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }

            preview?.let { p ->
                ImportPreviewDialog(
                    preview = p,
                    decisions = conflictDecisions,
                    onDecisionChange = { id, same ->
                        conflictDecisions = conflictDecisions + (id to same)
                    },
                    onConfirm = {
                        scope.launch {
                            busy = true
                            isError = false
                            statusMessage = null
                            try {
                                val merge = p.conflicts
                                    .filter { conflictDecisions[it.importedProductId] == true }
                                    .associate { it.importedProductId to it.existingProductId }
                                viewModel.confirmImport(p.data, merge)
                                statusMessage = context.getString(
                                    R.string.import_success,
                                    p.data.products.size,
                                    p.data.categories.size,
                                    p.data.shoppingLists.size
                                )
                            } catch (e: Exception) {
                                isError = true
                                statusMessage = errorMessage
                                // Debug: uncomment these two lines to surface the real cause
                                // (exception + message) on screen and in logcat.
                                // android.util.Log.e("PantryHubImport", "Import confirm failed", e)
                                // statusMessage = "$errorMessage\n${e.javaClass.simpleName}: ${e.message}"
                            } finally {
                                busy = false
                                preview = null
                                conflictDecisions = emptyMap()
                            }
                        }
                    },
                    onDismiss = {
                        preview = null
                        conflictDecisions = emptyMap()
                    }
                )
            }
        }
    }
}
