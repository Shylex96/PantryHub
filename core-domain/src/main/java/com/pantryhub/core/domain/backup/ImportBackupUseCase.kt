package com.pantryhub.core.domain.backup

import com.pantryhub.core.data.repository.BackupRepository
import com.pantryhub.core.model.backup.BackupData
import javax.inject.Inject

/**
 * Parses a JSON backup string and merges it into the local database.
 * Returns the parsed [BackupData] so the UI can report what was imported.
 */
class ImportBackupUseCase @Inject constructor(
    private val backupRepository: BackupRepository,
    private val importData: ImportDataUseCase
) {
    suspend operator fun invoke(json: String): BackupData {
        val data = backupRepository.deserialize(json)
        importData(data)
        return data
    }
}
