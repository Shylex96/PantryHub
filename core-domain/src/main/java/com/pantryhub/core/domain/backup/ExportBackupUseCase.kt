package com.pantryhub.core.domain.backup

import com.pantryhub.core.data.repository.BackupRepository
import javax.inject.Inject

/**
 * Builds the full backup and serializes it to a JSON string ready to be written
 * to a file / shared.
 */
class ExportBackupUseCase @Inject constructor(
    private val exportData: ExportDataUseCase,
    private val backupRepository: BackupRepository
) {
    suspend operator fun invoke(): String = backupRepository.serialize(exportData())
}
