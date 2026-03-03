package app.krafted.orbduel.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.orbduel.R
import app.krafted.orbduel.ui.theme.DarkBg
import app.krafted.orbduel.ui.theme.NeonCyan
import app.krafted.orbduel.ui.theme.NeonMagenta
import app.krafted.orbduel.ui.theme.NeonOrange

@Composable
fun HomeScreen(
    onPlayVsAi: () -> Unit,
    onPlayVsPlayer: () -> Unit,
    onLeaderboard: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "title")

    val bloomAlpha by infiniteTransition.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.26f,
        animationSpec = infiniteRepeatable(
            tween(1400, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "bloom"
    )

    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2800, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val titleBrush = if (shimmer < 0.05f || shimmer > 0.95f) {
        Brush.linearGradient(
            listOf(NeonMagenta, Color(0xFFFF88FF), Color.White, Color(0xFF88FFFF), NeonCyan)
        )
    } else {
        Brush.linearGradient(
            colorStops = arrayOf(
                0f to NeonMagenta,
                (shimmer - 0.15f).coerceAtLeast(0.01f) to Color(0xFFFF88FF),
                shimmer to Color.White,
                (shimmer + 0.15f).coerceAtMost(0.99f) to Color(0xFF88FFFF),
                1f to NeonCyan
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.bg_home),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBg.copy(alpha = 0.60f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, NeonCyan, NeonMagenta, Color.Transparent)
                        )
                    )
            )

            Spacer(Modifier.height(20.dp))

            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "ORB DUEL",
                    softWrap = false,
                    style = TextStyle(
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonMagenta.copy(alpha = bloomAlpha),
                        letterSpacing = 4.sp,
                        shadow = Shadow(color = NeonMagenta, blurRadius = 160f)
                    )
                )
                Text(
                    text = "ORB DUEL",
                    softWrap = false,
                    style = TextStyle(
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonCyan.copy(alpha = bloomAlpha * 0.6f),
                        letterSpacing = 4.sp,
                        shadow = Shadow(color = NeonCyan, blurRadius = 90f)
                    )
                )
                Text(
                    text = "ORB DUEL",
                    softWrap = false,
                    style = TextStyle(
                        brush = titleBrush,
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 4.sp,
                        shadow = Shadow(color = NeonMagenta, blurRadius = 20f)
                    )
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) {
                    Box(Modifier
                        .size(4.dp)
                        .background(NeonCyan, RoundedCornerShape(2.dp)))
                    Spacer(Modifier.width(4.dp))
                }
                Text(
                    text = "COSMIC BATTLE ARENA",
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 4.sp,
                        shadow = Shadow(color = NeonCyan, blurRadius = 14f)
                    )
                )
                repeat(3) {
                    Spacer(Modifier.width(4.dp))
                    Box(Modifier
                        .size(4.dp)
                        .background(NeonCyan, RoundedCornerShape(2.dp)))
                }
            }

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent, NeonMagenta,
                                NeonCyan, NeonMagenta, Color.Transparent
                            )
                        )
                    )
            )

            Spacer(Modifier.height(52.dp))

            GameButton(label = "PLAY  VS  AI", color = NeonMagenta, onClick = onPlayVsAi)
            Spacer(Modifier.height(14.dp))
            GameButton(label = "PLAY  VS  PLAYER", color = NeonCyan, onClick = onPlayVsPlayer)
            Spacer(Modifier.height(14.dp))
            GameButton(label = "LEADERBOARD", color = NeonOrange, onClick = onLeaderboard)
        }
    }
}

@Composable
private fun GameButton(
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    val buttonShape = RoundedCornerShape(4.dp)
    val gradient = Brush.verticalGradient(
        colors = listOf(color.copy(alpha = 0.30f), color.copy(alpha = 0.06f))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .shadow(
                elevation = 14.dp,
                shape = buttonShape,
                spotColor = color,
                ambientColor = color.copy(alpha = 0.45f)
            )
            .clip(buttonShape)
            .background(gradient)
            .border(1.5.dp, color, buttonShape)
            .clickable(onClick = onClick),
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
                    color = color,
                    shadow = Shadow(color = color, blurRadius = 12f)
                )
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 3.sp,
                    shadow = Shadow(color = color, blurRadius = 16f)
                )
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = "◆",
                style = TextStyle(
                    fontSize = 7.sp,
                    color = color,
                    shadow = Shadow(color = color, blurRadius = 12f)
                )
            )
        }
    }
}
