package app.krafted.orbduel.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.orbduel.game.Element
import app.krafted.orbduel.ui.theme.color
import app.krafted.orbduel.ui.theme.drawableRes

@Composable
fun OrbSelectionRow(
    selectedOrb: Element?,
    onOrbSelected: (Element) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(Element.entries) { element ->
            val isSelected = element == selectedOrb

            val targetAlpha = when {
                !enabled -> 0.3f
                isSelected -> 1f
                else -> 0.6f
            }
            val targetScale = if (isSelected) 1.2f else 1.0f

            val animatedAlpha by animateFloatAsState(
                targetValue = targetAlpha,
                animationSpec = tween(200),
                label = "orbAlpha"
            )

            val animatedScale by animateFloatAsState(
                targetValue = targetScale,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "orbScale"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(72.dp)
                    .clickable(enabled = enabled) { onOrbSelected(element) }
            ) {
                Image(
                    painter = painterResource(element.drawableRes),
                    contentDescription = element.displayName,
                    modifier = Modifier
                        .size(64.dp)
                        .graphicsLayer {
                            alpha = animatedAlpha
                            scaleX = animatedScale
                            scaleY = animatedScale
                        }
                        .then(
                            if (isSelected) {
                                Modifier
                                    .shadow(
                                        8.dp,
                                        CircleShape,
                                        ambientColor = element.color,
                                        spotColor = element.color
                                    )
                                    .border(2.dp, element.color, CircleShape)
                            } else {
                                Modifier
                            }
                        )
                        .clip(CircleShape)
                )
                Text(
                    text = element.displayName,
                    fontSize = 9.sp,
                    color = element.color.copy(alpha = animatedAlpha)
                )
            }
        }
    }
}
