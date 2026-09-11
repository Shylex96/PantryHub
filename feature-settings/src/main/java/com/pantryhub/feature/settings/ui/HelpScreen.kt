package com.pantryhub.feature.settings.ui

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryHeaderIconButton
import com.pantryhub.core.designsystem.ui.components.PantryScreenHeader
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme

/** A single help topic: a short title and the explanatory body shown when expanded. */
private data class HelpTopic(
    @StringRes val title: Int,
    @StringRes val body: Int
)

private val helpTopics = listOf(
    HelpTopic(R.string.help_lists_title, R.string.help_lists_body),
    HelpTopic(R.string.help_list_types_title, R.string.help_list_types_body),
    HelpTopic(R.string.help_templates_title, R.string.help_templates_body),
    HelpTopic(R.string.help_shopping_mode_title, R.string.help_shopping_mode_body),
    HelpTopic(R.string.help_products_title, R.string.help_products_body),
    HelpTopic(R.string.help_categories_title, R.string.help_categories_body),
    HelpTopic(R.string.help_aliases_title, R.string.help_aliases_body),
    HelpTopic(R.string.help_notes_title, R.string.help_notes_body),
    HelpTopic(R.string.help_backup_title, R.string.help_backup_body),
    HelpTopic(R.string.help_settings_title, R.string.help_settings_body)
)

@Composable
fun HelpScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = PantryHubTheme.spacing

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = spacing.xxl)
        ) {
            // Compact top bar (docs/05 §6.6): back only; the title lives in the header.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.lg, vertical = spacing.sm)
            ) {
                PantryHeaderIconButton(
                    icon = PantryIcons.Back,
                    contentDescription = stringResource(R.string.back_description),
                    onClick = onBack
                )
            }
            PantryScreenHeader(
                title = stringResource(R.string.help_screen_title),
                subtitle = stringResource(R.string.help_intro)
            )
            Spacer(modifier = Modifier.height(spacing.xl))

            Column(
                modifier = Modifier.padding(horizontal = spacing.screen),
                verticalArrangement = Arrangement.spacedBy(spacing.sm)
            ) {
                helpTopics.forEach { topic -> HelpTopicCard(topic) }
            }
        }
    }
}

@Composable
private fun HelpTopicCard(topic: HelpTopic) {
    val spacing = PantryHubTheme.spacing
    var expanded by remember { mutableStateOf(false) }

    Surface(
        onClick = { expanded = !expanded },
        shape = RoundedCornerShape(PantryHubTheme.radius.card),
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(spacing.lg)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(topic.title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) PantryIcons.ExpandLess else PantryIcons.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(spacing.sm))
                Text(
                    text = stringResource(topic.body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
