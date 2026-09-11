package com.pantryhub.core.designsystem.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme

/**
 * The app-wide swipe treatment for item rows (docs/05_Design_System.md §6.3, §7):
 * swipe **left** = delete (danger background, the row is dismissed), swipe **right** =
 * toggle favorite (gold background, the row springs back). Pass `onFavorite = null` to
 * disable the right swipe. With [dismissOnDelete] = false the row springs back after
 * [onDelete] instead of disappearing — use it when deletion still needs a confirmation.
 */
@OptIn(ExperimentalMaterial3Api::class)
// SwipeToDismiss confirmValueChange is deprecated without a drop-in replacement.
@Suppress("DEPRECATION")
@Composable
fun PantrySwipeRow(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    onFavorite: (() -> Unit)? = null,
    dismissOnDelete: Boolean = true,
    content: @Composable () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    val shape = RoundedCornerShape(PantryHubTheme.radius.row)
    val favoriteColor = PantryHubTheme.extendedColors.favorite
    val onFavoriteColor = PantryHubTheme.extendedColors.onFavorite
    val deleteColor = MaterialTheme.colorScheme.error

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete()
                    dismissOnDelete
                }
                SwipeToDismissBoxValue.StartToEnd -> {
                    onFavorite?.invoke()
                    false
                }
                else -> false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = onFavorite != null,
        enableDismissFromEndToStart = true,
        modifier = modifier,
        backgroundContent = {
            val progress = dismissState.progress
            val alpha = (progress * 2f).coerceAtMost(1f)
            val scale = 0.6f + (progress * 0.4f).coerceAtMost(0.4f)
            when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(deleteColor.copy(alpha = alpha), shape)
                        .padding(horizontal = spacing.xl),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = PantryIcons.Delete,
                        contentDescription = stringResource(R.string.delete_action),
                        tint = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.scale(scale)
                    )
                }
                SwipeToDismissBoxValue.StartToEnd -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(favoriteColor.copy(alpha = alpha), shape)
                        .padding(horizontal = spacing.xl),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Icon(
                        imageVector = PantryIcons.Favorite,
                        contentDescription = null,
                        tint = onFavoriteColor,
                        modifier = Modifier.scale(scale)
                    )
                }
                SwipeToDismissBoxValue.Settled -> Unit
            }
        }
    ) {
        content()
    }
}
