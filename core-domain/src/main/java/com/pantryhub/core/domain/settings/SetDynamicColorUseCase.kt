package com.pantryhub.core.domain.settings

import com.pantryhub.core.data.repository.SettingsRepository
import javax.inject.Inject

class SetDynamicColorUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean) = repository.setDynamicColor(enabled)
}
