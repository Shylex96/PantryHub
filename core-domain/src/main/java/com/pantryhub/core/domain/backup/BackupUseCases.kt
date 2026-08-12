package com.pantryhub.core.domain.backup

import javax.inject.Inject

class BackupUseCases @Inject constructor(
    val exportData: ExportDataUseCase,
    val importData: ImportDataUseCase
)
