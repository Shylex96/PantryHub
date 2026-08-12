package com.pantryhub.core.domain.settings

import com.pantryhub.core.data.repository.SettingsRepository
import com.pantryhub.core.model.settings.ThemeMode
import javax.inject.Inject

class SetThemeModeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(mode: ThemeMode) = repository.setThemeMode(mode)
}
