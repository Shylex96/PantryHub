package com.pantryhub.core.data.repository

import com.pantryhub.core.model.backup.BackupData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class OfflineBackupRepository @Inject constructor() : BackupRepository {
    
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    override fun serialize(data: BackupData): String {
        return json.encodeToString(data)
    }

    override fun deserialize(jsonString: String): BackupData {
        return json.decodeFromString(jsonString)
    }
}
