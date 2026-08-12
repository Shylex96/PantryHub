package com.pantryhub.core.domain.settings

import com.pantryhub.core.data.repository.SettingsRepository
import com.pantryhub.core.model.settings.AppSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<AppSettings> = repository.settings
}
