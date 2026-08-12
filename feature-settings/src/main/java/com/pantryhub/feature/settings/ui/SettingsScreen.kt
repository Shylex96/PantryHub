package com.pantryhub.feature.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryCard
import com.pantryhub.core.designsystem.ui.components.PantryListItem
import com.pantryhub.core.designsystem.ui.components.PantryTopBar
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.model.settings.ThemeMode
import com.pantryhub.feature.settings.presentation.SettingsViewModel

@Composable
fun SettingsScreen(
    onOpenImportExport: () -> Unit,
    onOpenHelp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val settings by viewModel.settings.collectAsState()
    val spacing = PantryHubTheme.spacing

    // Per-app language via AppCompat; persisted automatically (see manifest service).
    // Changing it recreates the activity, so the selection re-reads on recomposition.
    fun applyLanguage(tag: String) {
        val locales = if (tag.isEmpty()) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(tag)
        }
        AppCompatDelegate.setApplicationLocales(locales)
    }

    Scaffold(
        topBar = { PantryTopBar(title = stringResource(R.string.nav_settings)) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = spacing.lg, vertical = spacing.md)
        ) {
            SectionHeader(stringResource(R.string.settings_appearance))

            PantryCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    ThemeMode.entries.forEach { mode ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.setThemeMode(mode) }
                                .padding(horizontal = spacing.lg, vertical = spacing.sm)
                        ) {
                            RadioButton(
                                selected = settings.themeMode == mode,
                                onClick = { viewModel.setThemeMode(mode) }
                            )
                            Spacer(modifier = Modifier.width(spacing.md))
                            Text(
                                text = stringResource(themeModeLabel(mode)),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = spacing.lg, vertical = spacing.sm)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.dynamic_color_label),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = stringResource(R.string.dynamic_color_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = settings.dynamicColor,
                            onCheckedChange = { viewModel.setDynamicColor(it) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.size(spacing.lg))

            SectionHeader(stringResource(R.string.settings_language))

            PantryCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    val currentLanguage = run {
                        val locales = AppCompatDelegate.getApplicationLocales()
                        if (locales.isEmpty) "" else (locales.get(0)?.language ?: "")
                    }
                    listOf("", "es", "en").forEach { tag ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { applyLanguage(tag) }
                                .padding(horizontal = spacing.lg, vertical = spacing.sm)
                        ) {
                            RadioButton(
                                selected = currentLanguage == tag,
                                onClick = { applyLanguage(tag) }
                            )
                            Spacer(modifier = Modifier.width(spacing.md))
                            Text(
                                text = stringResource(languageLabel(tag)),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.size(spacing.lg))

            SectionHeader(stringResource(R.string.settings_data))

            PantryCard(modifier = Modifier.fillMaxWidth()) {
                PantryListItem(
                    title = stringResource(R.string.backup_section_title),
                    subtitle = stringResource(R.string.settings_backup_row_desc),
                    onClick = onOpenImportExport
                )
            }

            Spacer(modifier = Modifier.size(spacing.lg))

            SectionHeader(stringResource(R.string.settings_help))

            PantryCard(modifier = Modifier.fillMaxWidth()) {
                PantryListItem(
                    title = stringResource(R.string.help_row_title),
                    subtitle = stringResource(R.string.help_row_desc),
                    onClick = onOpenHelp
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(
            start = PantryHubTheme.spacing.sm,
            bottom = PantryHubTheme.spacing.sm
        )
    )
}

private fun themeModeLabel(mode: ThemeMode): Int = when (mode) {
    ThemeMode.SYSTEM -> R.string.theme_system
    ThemeMode.LIGHT -> R.string.theme_light
    ThemeMode.DARK -> R.string.theme_dark
}

private fun languageLabel(tag: String): Int = when (tag) {
    "es" -> R.string.language_es
    "en" -> R.string.language_en
    else -> R.string.language_system
}
