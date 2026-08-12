package com.pantryhub.feature.importexport.presentation

import androidx.lifecycle.ViewModel
import com.pantryhub.core.domain.backup.BackupUseCases
import com.pantryhub.core.domain.backup.ImportPreview
import com.pantryhub.core.model.backup.BackupData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImportExportViewModel @Inject constructor(
    private val backupUseCases: BackupUseCases
) : ViewModel() {

    /** Builds the full backup as a JSON string. */
    suspend fun buildBackupJson(): String = backupUseCases.exportBackup()

    /** Parses a backup and classifies products (new / auto-merge / conflicts) for preview. */
    suspend fun analyzeImport(json: String): ImportPreview = backupUseCases.analyzeImport(json)

    /** Applies the import once the user has resolved any conflicts. */
    suspend fun confirmImport(data: BackupData, mergeDecisions: Map<String, String>) =
        backupUseCases.importData(data, mergeDecisions)
}
