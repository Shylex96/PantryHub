package com.pantryhub.core.domain.settings

import javax.inject.Inject

class SettingsUseCases @Inject constructor(
    val getSettings: GetSettingsUseCase,
    val setThemeMode: SetThemeModeUseCase,
    val setDynamicColor: SetDynamicColorUseCase
)
