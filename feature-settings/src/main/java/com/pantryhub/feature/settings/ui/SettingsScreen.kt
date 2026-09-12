package com.pantryhub.feature.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryButton
import com.pantryhub.core.designsystem.ui.components.PantryButtonType
import com.pantryhub.core.designsystem.ui.components.PantryDialog
import com.pantryhub.core.designsystem.ui.components.PantryOptionRow
import com.pantryhub.core.designsystem.ui.components.PantryScreenHeader
import com.pantryhub.core.designsystem.ui.components.PantrySectionLabel
import com.pantryhub.core.designsystem.ui.components.PantrySheet
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.domain.settings.DataScope
import com.pantryhub.core.model.settings.ThemeMode
import com.pantryhub.feature.settings.presentation.SettingsViewModel

/**
 * Settings tab (docs/05_Design_System.md §6.1): large header, uppercase section labels
 * and grouped cards of 60dp rows with a tinted icon tile, title, current value and a
 * chevron. Choices open bottom sheets; "Manage data" is the bulk-delete / start-over tool.
 */
@Composable
fun SettingsScreen(
    onOpenImportExport: () -> Unit,
    onOpenHelp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val settings by viewModel.settings.collectAsState()
    val isClearingData by viewModel.isClearingData.collectAsState()
    val dataCleared by viewModel.dataCleared.collectAsState()
    val spacing = PantryHubTheme.spacing
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var showThemeSheet by remember { mutableStateOf(false) }
    var showLanguageSheet by remember { mutableStateOf(false) }
    var showManageDataSheet by remember { mutableStateOf(false) }
    var showAboutSheet by remember { mutableStateOf(false) }

    val appVersion = remember(context) {
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }.getOrNull() ?: ""
    }

    // Per-app language via AppCompat; persisted automatically (see manifest service).
    // An empty locale list = follow the system, which is the default on a fresh install.
    // Changing it recreates the activity, so the selection re-reads on recomposition.
    val currentLanguage = run {
        val locales = AppCompatDelegate.getApplicationLocales()
        if (locales.isEmpty) AppLanguages.SYSTEM else (locales.get(0)?.language ?: AppLanguages.SYSTEM)
    }
    fun applyLanguage(tag: String) {
        val locales = if (tag == AppLanguages.SYSTEM) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(tag)
        }
        AppCompatDelegate.setApplicationLocales(locales)
    }

    val dataClearedMessage = stringResource(R.string.manage_data_done)
    LaunchedEffect(dataCleared) {
        if (dataCleared) {
            showManageDataSheet = false
            viewModel.consumeDataCleared()
            snackbarHostState.showSnackbar(dataClearedMessage)
        }
    }

    if (showThemeSheet) {
        ThemeSheet(
            current = settings.themeMode,
            onSelect = {
                viewModel.setThemeMode(it)
                showThemeSheet = false
            },
            onDismiss = { showThemeSheet = false }
        )
    }

    if (showLanguageSheet) {
        LanguageSheet(
            current = currentLanguage,
            onSelect = {
                showLanguageSheet = false
                applyLanguage(it)
            },
            onDismiss = { showLanguageSheet = false }
        )
    }

    if (showAboutSheet) {
        AboutSheet(version = appVersion, onDismiss = { showAboutSheet = false })
    }

    if (showManageDataSheet) {
        ManageDataSheet(
            isWorking = isClearingData,
            onConfirm = { viewModel.clearData(it) },
            onDismiss = { if (!isClearingData) showManageDataSheet = false }
        )
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = spacing.xxl)
        ) {
            PantryScreenHeader(
                title = stringResource(R.string.nav_settings),
                subtitle = stringResource(R.string.settings_subtitle),
                modifier = Modifier.padding(top = spacing.lg)
            )
            Spacer(modifier = Modifier.height(spacing.xl))

            SettingsSection(title = stringResource(R.string.settings_appearance)) {
                SettingsRow(
                    icon = PantryIcons.Theme,
                    tileColor = MaterialTheme.colorScheme.primaryContainer,
                    onTileColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    title = stringResource(R.string.settings_theme),
                    value = stringResource(themeModeLabel(settings.themeMode)),
                    onClick = { showThemeSheet = true }
                )
                SettingsDivider()
                SettingsRow(
                    icon = PantryIcons.Palette,
                    tileColor = MaterialTheme.colorScheme.secondaryContainer,
                    onTileColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    title = stringResource(R.string.dynamic_color_label),
                    subtitle = stringResource(R.string.dynamic_color_desc),
                    trailing = {
                        Switch(
                            checked = settings.dynamicColor,
                            onCheckedChange = { viewModel.setDynamicColor(it) }
                        )
                    }
                )
            }

            SettingsSection(title = stringResource(R.string.settings_language)) {
                SettingsRow(
                    icon = PantryIcons.Language,
                    tileColor = MaterialTheme.colorScheme.tertiaryContainer,
                    onTileColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    title = stringResource(R.string.settings_app_language),
                    value = languageLabel(currentLanguage),
                    onClick = { showLanguageSheet = true }
                )
            }

            SettingsSection(title = stringResource(R.string.settings_data)) {
                SettingsRow(
                    icon = PantryIcons.Backup,
                    tileColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    onTileColor = MaterialTheme.colorScheme.onSurface,
                    title = stringResource(R.string.backup_section_title),
                    subtitle = stringResource(R.string.settings_backup_row_desc),
                    onClick = onOpenImportExport
                )
                SettingsDivider()
                SettingsRow(
                    icon = PantryIcons.DeleteSweep,
                    tileColor = MaterialTheme.colorScheme.errorContainer,
                    onTileColor = MaterialTheme.colorScheme.onErrorContainer,
                    title = stringResource(R.string.manage_data_title),
                    subtitle = stringResource(R.string.manage_data_row_desc),
                    onClick = { showManageDataSheet = true }
                )
            }

            SettingsSection(title = stringResource(R.string.settings_help)) {
                SettingsRow(
                    icon = PantryIcons.Help,
                    tileColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    onTileColor = MaterialTheme.colorScheme.onSurface,
                    title = stringResource(R.string.help_row_title),
                    subtitle = stringResource(R.string.help_row_desc),
                    onClick = onOpenHelp
                )
                SettingsDivider()
                SettingsRow(
                    icon = PantryIcons.Info,
                    tileColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    onTileColor = MaterialTheme.colorScheme.onSurface,
                    title = stringResource(R.string.about_title),
                    value = appVersion,
                    onClick = { showAboutSheet = true }
                )
            }
        }
    }
}

/** Uppercase label + one rounded card grouping the rows. */
@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    Column(modifier = Modifier.padding(horizontal = spacing.screen)) {
        PantrySectionLabel(text = title)
        Spacer(modifier = Modifier.height(spacing.sm))
        Surface(
            shape = RoundedCornerShape(PantryHubTheme.radius.card),
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column { content() }
        }
        Spacer(modifier = Modifier.height(spacing.xl))
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant,
        modifier = Modifier.padding(start = 66.dp, end = 16.dp)
    )
}

/**
 * 60dp row: 36dp tinted icon tile · title (+ optional subtitle) · optional value ·
 * chevron when clickable, or a custom [trailing] control.
 */
@Composable
private fun SettingsRow(
    icon: ImageVector,
    tileColor: Color,
    onTileColor: Color,
    title: String,
    subtitle: String? = null,
    value: String? = null,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    val rowModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp)

    val content: @Composable () -> Unit = {
        Row(
            modifier = rowModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(11.dp),
                color = tileColor,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = onTileColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            if (value != null) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
            when {
                trailing != null -> trailing()
                onClick != null -> Icon(
                    imageVector = PantryIcons.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    if (onClick != null) {
        Surface(onClick = onClick, color = Color.Transparent) { content() }
    } else {
        content()
    }
}

@Composable
private fun ThemeSheet(
    current: ThemeMode,
    onSelect: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    PantrySheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.settings_theme)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            ThemeMode.entries.forEach { mode ->
                PantryOptionRow(
                    label = stringResource(themeModeLabel(mode)),
                    selected = mode == current,
                    onClick = { onSelect(mode) }
                )
            }
        }
    }
}

@Composable
private fun LanguageSheet(
    current: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    PantrySheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.settings_app_language),
        subtitle = stringResource(R.string.settings_language_hint)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            AppLanguages.supported.forEach { tag ->
                PantryOptionRow(
                    label = languageLabel(tag),
                    selected = tag == current,
                    onClick = { onSelect(tag) }
                )
            }
        }
    }
}

/**
 * An optional external link shown as a button at the bottom of the About sheet — e.g. a
 * Play Store shortcut to the author's other apps. `null` hides the button entirely (the
 * current state); the wiring stays so it can be switched on without UI work.
 */
private data class AboutLink(val label: String, val url: String)

/** About PantryHub: what it is, where the data lives, version and (optionally) a link. */
@Composable
private fun AboutSheet(
    version: String,
    onDismiss: () -> Unit,
    link: AboutLink? = null
) {
    val spacing = PantryHubTheme.spacing
    val context = LocalContext.current

    PantrySheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.about_title),
        subtitle = stringResource(R.string.about_version, version)
    ) {
        Text(
            text = stringResource(R.string.about_body),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(spacing.md))
        Text(
            text = stringResource(R.string.about_privacy),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(spacing.md))
        Text(
            text = stringResource(R.string.about_credits),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (link != null) {
            Spacer(modifier = Modifier.height(spacing.xl))
            PantryButton(
                onClick = {
                    runCatching {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link.url)))
                    }
                },
                type = PantryButtonType.Secondary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(link.label)
            }
        }
    }
}

/**
 * Manage data: pick any combination of scopes (or "Everything"), then confirm. The wipe
 * itself runs in the ViewModel; the sheet closes when it reports completion.
 */
@Composable
private fun ManageDataSheet(
    isWorking: Boolean,
    onConfirm: (Set<DataScope>) -> Unit,
    onDismiss: () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    var selected by remember { mutableStateOf<Set<DataScope>>(emptySet()) }
    var showConfirm by remember { mutableStateOf(false) }
    val allScopes = DataScope.entries.toSet()
    val everything = selected == allScopes

    if (showConfirm) {
        PantryDialog(
            onDismissRequest = { showConfirm = false },
            title = stringResource(R.string.manage_data_confirm_title),
            confirmButton = {
                TextButton(onClick = {
                    showConfirm = false
                    onConfirm(selected)
                }) {
                    Text(
                        text = stringResource(R.string.delete_confirm_action),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text(stringResource(R.string.cancel_action))
                }
            }
        ) {
            val names = selected
                .sortedBy { it.ordinal }
                .map { stringResource(dataScopeLabel(it)) }
                .joinToString(", ")
            Text(stringResource(R.string.manage_data_confirm_message, names))
        }
    }

    PantrySheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.manage_data_title),
        subtitle = stringResource(R.string.manage_data_subtitle)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            PantryOptionRow(
                label = stringResource(R.string.manage_data_everything),
                selected = everything,
                onClick = { selected = if (everything) emptySet() else allScopes }
            )
            DataScope.entries.forEach { scope ->
                PantryOptionRow(
                    label = stringResource(dataScopeLabel(scope)),
                    selected = scope in selected,
                    onClick = {
                        selected = if (scope in selected) selected - scope else selected + scope
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(spacing.sm))
        Text(
            text = stringResource(R.string.manage_data_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(horizontal = spacing.xs)
        )
        Spacer(modifier = Modifier.height(spacing.xl))
        PantryButton(
            onClick = { showConfirm = true },
            type = PantryButtonType.Destructive,
            enabled = selected.isNotEmpty(),
            isLoading = isWorking,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = PantryIcons.Delete,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(spacing.sm))
            Text(stringResource(R.string.manage_data_delete_action))
        }
    }
}

private fun dataScopeLabel(scope: DataScope): Int = when (scope) {
    DataScope.LISTS -> R.string.manage_data_lists
    DataScope.PRODUCTS -> R.string.manage_data_products
    DataScope.CATEGORIES -> R.string.manage_data_categories
    DataScope.NOTES -> R.string.manage_data_notes
    DataScope.HISTORY -> R.string.manage_data_history
    DataScope.FAVORITES -> R.string.manage_data_favorites
}

private fun themeModeLabel(mode: ThemeMode): Int = when (mode) {
    ThemeMode.SYSTEM -> R.string.theme_system
    ThemeMode.LIGHT -> R.string.theme_light
    ThemeMode.DARK -> R.string.theme_dark
}

/** "System default" for the empty tag, otherwise the language's own name ("Español"). */
@Composable
private fun languageLabel(tag: String): String =
    if (tag == AppLanguages.SYSTEM) stringResource(R.string.language_system) else AppLanguages.nativeName(tag)
