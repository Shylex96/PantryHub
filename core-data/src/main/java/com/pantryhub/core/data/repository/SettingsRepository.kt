package com.pantryhub.core.data.repository

import com.pantryhub.core.model.settings.AppSettings
import com.pantryhub.core.model.settings.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<AppSettings>
    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setDynamicColor(enabled: Boolean)
}
