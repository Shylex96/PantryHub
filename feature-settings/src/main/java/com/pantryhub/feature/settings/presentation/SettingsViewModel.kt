package com.pantryhub.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantryhub.core.domain.settings.DataScope
import com.pantryhub.core.domain.settings.SettingsUseCases
import com.pantryhub.core.model.settings.AppSettings
import com.pantryhub.core.model.settings.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    val settings: StateFlow<AppSettings> = settingsUseCases.getSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppSettings())

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { settingsUseCases.setThemeMode(mode) }
    }

    fun setDynamicColor(enabled: Boolean) {
        viewModelScope.launch { settingsUseCases.setDynamicColor(enabled) }
    }

    /** True while a Manage-data wipe is running; the sheet disables its button meanwhile. */
    private val _isClearingData = MutableStateFlow(false)
    val isClearingData: StateFlow<Boolean> = _isClearingData.asStateFlow()

    /** One-shot event: a wipe finished (the UI shows a confirmation and resets). */
    private val _dataCleared = MutableStateFlow(false)
    val dataCleared: StateFlow<Boolean> = _dataCleared.asStateFlow()

    fun clearData(scopes: Set<DataScope>) {
        if (scopes.isEmpty() || _isClearingData.value) return
        viewModelScope.launch {
            _isClearingData.update { true }
            try {
                settingsUseCases.clearData(scopes)
                _dataCleared.update { true }
            } finally {
                _isClearingData.update { false }
            }
        }
    }

    fun consumeDataCleared() {
        _dataCleared.update { false }
    }
}
