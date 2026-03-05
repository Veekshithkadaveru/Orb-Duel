package app.krafted.orbduel.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun rememberShimmerBrush(
    accentColor: Color,
    secondaryColor: Color = accentColor,
    shimmerDurationMs: Int = 2800,
    label: String = "shimmer"
): Brush {
    val infiniteTransition = rememberInfiniteTransition(label = label)

    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(shimmerDurationMs, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "${label}Value"
    )

    return if (shimmer < 0.05f || shimmer > 0.95f) {
        Brush.linearGradient(listOf(accentColor, Color.White, secondaryColor))
    } else {
        Brush.linearGradient(
            colorStops = arrayOf(
                0f to accentColor,
                (shimmer - 0.15f).coerceAtLeast(0.01f) to accentColor.copy(alpha = 0.7f),
                shimmer to Color.White,
                (shimmer + 0.15f).coerceAtMost(0.99f) to secondaryColor.copy(alpha = 0.7f),
                1f to secondaryColor
            )
        )
    }
}

@Composable
fun rememberBloomAlpha(
    minAlpha: Float = 0.08f,
    maxAlpha: Float = 0.26f,
    durationMs: Int = 1400,
    label: String = "bloom"
): Float {
    val infiniteTransition = rememberInfiniteTransition(label = label)

    val bloomAlpha by infiniteTransition.animateFloat(
        initialValue = minAlpha,
        targetValue = maxAlpha,
        animationSpec = infiniteRepeatable(
            tween(durationMs, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "${label}Value"
    )

    return bloomAlpha
}
