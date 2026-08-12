package com.pantryhub.core.data.repository

import com.pantryhub.core.model.backup.BackupData

interface BackupRepository {
    fun serialize(data: BackupData): String
    fun deserialize(json: String): BackupData
}
