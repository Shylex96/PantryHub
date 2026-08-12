package com.pantryhub.feature.settings.ui

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryCard
import com.pantryhub.core.designsystem.ui.components.PantryTopBar
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
        topBar = {
            PantryTopBar(
                title = stringResource(R.string.help_screen_title),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = PantryIcons.Back,
                            contentDescription = stringResource(R.string.back_description)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = spacing.lg, vertical = spacing.md)
        ) {
            Text(
                text = stringResource(R.string.help_intro),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(
                    horizontal = spacing.sm,
                    vertical = spacing.sm
                )
            )

            helpTopics.forEach { topic ->
                HelpTopicCard(topic)
                Spacer(modifier = Modifier.size(spacing.sm))
            }
        }
    }
}

@Composable
private fun HelpTopicCard(topic: HelpTopic) {
    val spacing = PantryHubTheme.spacing
    var expanded by remember { mutableStateOf(false) }

    PantryCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .animateContentSize()
                .padding(spacing.lg)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(topic.title),
                    style = MaterialTheme.typography.titleMedium,
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
                Spacer(modifier = Modifier.size(spacing.sm))
                Text(
                    text = stringResource(topic.body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
