package com.pantryhub.core.designsystem.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pantryhub.core.designsystem.ui.icons.PantryIcons
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import kotlinx.coroutines.delay

/**
 * Lightweight confirmation notices, anchored top-right (docs/05_Design_System.md §7).
 *
 * They exist so that a completed action can confirm itself without repainting the control
 * that triggered it: the sheet closes, a small pill slides in from the right, and it is gone
 * in two seconds. Use them for *successful, non-reversible-by-undo* actions ("List created",
 * "Category renamed"). Deletions keep the snackbar-with-Undo pattern (docs/04 "Undo
 * Pattern"), because a toast has nothing to tap.
 *
 * There is exactly one host, mounted once by the app above the navigation graph; screens
 * reach it through [LocalPantryToast] and never build their own.
 */
enum class PantryToastType {
    Success, Error, Info
}

data class PantryToastData(
    val message: String,
    val type: PantryToastType = PantryToastType.Success,
    val duration: Long = 2000L,
    /** Distinguishes two consecutive, identical toasts so the second one still animates. */
    val id: Long = 0L
)

@Stable
class PantryToastState {
    var currentToast by mutableStateOf<PantryToastData?>(null)
        private set

    private var nextId = 0L

    fun show(message: String, type: PantryToastType = PantryToastType.Success) {
        currentToast = PantryToastData(message = message, type = type, id = nextId++)
    }

    fun dismiss() {
        currentToast = null
    }
}

@Composable
fun rememberPantryToastState(): PantryToastState = remember { PantryToastState() }

/**
 * The toast state for the current screen. Provided once by the app; the default throws so a
 * missing provider fails loudly in development instead of silently swallowing confirmations.
 */
val LocalPantryToast = compositionLocalOf<PantryToastState> {
    error("No PantryToastState provided. Wrap the app in PantryToastHost { ... }.")
}

/**
 * Mounts the single toast layer over [content] and provides it through [LocalPantryToast].
 * Call this once, in the app composition root, outside the NavHost.
 */
@Composable
fun PantryToastHost(
    modifier: Modifier = Modifier,
    state: PantryToastState = rememberPantryToastState(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalPantryToast provides state) {
        Box(modifier = modifier.fillMaxSize()) {
            content()
            PantryToastLayer(state = state)
        }
    }
}

/** The overlay itself. Public so a preview or a standalone screen can place it directly. */
@Composable
fun PantryToastLayer(
    state: PantryToastState,
    modifier: Modifier = Modifier
) {
    val toast = state.currentToast

    LaunchedEffect(toast?.id) {
        if (toast != null) {
            delay(toast.duration)
            state.dismiss()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top = 12.dp, end = 16.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        AnimatedVisibility(
            visible = toast != null,
            // Entrance 200ms / exit 150ms: fast enough not to read as a screen transition
            // (docs/05_Design_System.md §7 — micro 100, standard 250).
            enter = slideInHorizontally(animationSpec = tween(200)) { it } + fadeIn(tween(200)),
            exit = slideOutHorizontally(animationSpec = tween(150)) { it } + fadeOut(tween(150))
        ) {
            if (toast != null) {
                PantryToast(toast)
            }
        }
    }
}

@Composable
private fun PantryToast(data: PantryToastData) {
    val extendedColors = PantryHubTheme.extendedColors
    val (bgColor, icon, iconColor) = when (data.type) {
        PantryToastType.Success -> Triple(
            extendedColors.success.copy(alpha = 0.95f),
            PantryIcons.Check,
            extendedColors.onStatus
        )
        PantryToastType.Error -> Triple(
            MaterialTheme.colorScheme.error.copy(alpha = 0.95f),
            PantryIcons.Close,
            MaterialTheme.colorScheme.onError
        )
        PantryToastType.Info -> Triple(
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.95f),
            PantryIcons.Help,
            MaterialTheme.colorScheme.onSecondary
        )
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 4.dp,
        shadowElevation = 6.dp,
        modifier = Modifier.widthIn(max = 280.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = data.message,
                style = MaterialTheme.typography.bodyMedium,
                color = iconColor,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.2.sp
            )
        }
    }
}
