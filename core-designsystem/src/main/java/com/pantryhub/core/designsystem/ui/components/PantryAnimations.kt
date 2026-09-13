package com.pantryhub.core.designsystem.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

/**
 * A modifier that shakes the element horizontally when [trigger] changes.
 * Useful for validation feedback.
 */
fun Modifier.shake(trigger: Int) = composed {
    val offset = remember { Animatable(0f) }

    LaunchedEffect(trigger) {
        if (trigger > 0) {
            // Shake sequence: aggressive side-to-side agitation
            offset.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = 0.15f, // Low damping for several clear oscillations
                    stiffness = 2500f     // High stiffness for very fast agitation
                ),
                initialVelocity = 5000f   // High velocity for wide displacement
            )
        }
    }

    this.offset { IntOffset(offset.value.roundToInt(), 0) }
}
