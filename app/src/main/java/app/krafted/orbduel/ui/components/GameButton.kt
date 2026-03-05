package app.krafted.orbduel.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameButton(
    label: String,
    color: Color,
    enabled: Boolean = true,
    height: Dp = 58.dp,
    infinitePulse: Boolean = false,
    onClick: () -> Unit
) {
    val buttonShape = RoundedCornerShape(4.dp)
    
    val pulseAlpha = if (infinitePulse && enabled) {
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val alpha by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 0.6f,
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseAlpha"
        )
        alpha
    } else {
        if (enabled) 1f else 0.4f
    }

    val baseAlpha = if (enabled) 1f else 0.35f
    val displayColor = color.copy(alpha = baseAlpha)

    val gradient = Brush.verticalGradient(
        colors = listOf(displayColor.copy(alpha = 0.30f), displayColor.copy(alpha = 0.06f))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .shadow(
                elevation = if (enabled) 14.dp else 4.dp,
                shape = buttonShape,
                spotColor = displayColor,
                ambientColor = displayColor.copy(alpha = 0.45f)
            )
            .clip(buttonShape)
            .background(gradient)
            .border(1.5.dp, displayColor, buttonShape)
            .then(
                if (enabled) Modifier.clickable(onClick = onClick) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "◆",
                style = TextStyle(
                    fontSize = 7.sp,
                    color = displayColor,
                    shadow = Shadow(color = displayColor, blurRadius = 12f)
                )
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = pulseAlpha),
                    letterSpacing = 3.sp,
                    shadow = Shadow(color = displayColor, blurRadius = 16f)
                )
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = "◆",
                style = TextStyle(
                    fontSize = 7.sp,
                    color = displayColor,
                    shadow = Shadow(color = displayColor, blurRadius = 12f)
                )
            )
        }
    }
}
