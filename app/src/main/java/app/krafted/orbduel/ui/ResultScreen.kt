package app.krafted.orbduel.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.krafted.orbduel.R
import app.krafted.orbduel.game.GameMode
import app.krafted.orbduel.game.Player
import app.krafted.orbduel.ui.theme.CardSurface
import app.krafted.orbduel.ui.theme.DarkBg
import app.krafted.orbduel.ui.theme.GlowYellow
import app.krafted.orbduel.ui.theme.NeonCyan
import app.krafted.orbduel.ui.theme.NeonGreen
import app.krafted.orbduel.ui.theme.NeonMagenta
import app.krafted.orbduel.ui.theme.NeonOrange
import app.krafted.orbduel.viewmodel.BattleViewModel
import kotlinx.coroutines.delay

@Composable
fun ResultScreen(
    viewModel: BattleViewModel,
    onPlayAgain: () -> Unit,
    onMainMenu: () -> Unit,
    onLeaderboard: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val winnerName = when (uiState.matchWinner) {
        Player.PLAYER1 -> "PLAYER 1"
        Player.PLAYER2 -> uiState.player2Name.uppercase()
        null -> null
    }

    val isVictory = uiState.matchWinner == Player.PLAYER1
    val isDraw = uiState.matchWinner == null
    val accentColor = when {
        isDraw -> NeonOrange
        isVictory -> GlowYellow
        else -> NeonCyan
    }

    val infiniteTransition = rememberInfiniteTransition(label = "result")

    val bloomAlpha by infiniteTransition.animateFloat(
        initialValue = 0.06f,
        targetValue = 0.22f,
        animationSpec = infiniteRepeatable(
            tween(1600, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "bloom"
    )

    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(3000, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val titleScale = remember { Animatable(0f) }
    var showStats by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        titleScale.animateTo(1f, spring(dampingRatio = 0.55f, stiffness = 120f))
        delay(300)
        showStats = true
        delay(400)
        showButtons = true
    }

    val titleBrush = if (shimmer < 0.05f || shimmer > 0.95f) {
        Brush.linearGradient(listOf(accentColor, Color.White, accentColor))
    } else {
        Brush.linearGradient(
            colorStops = arrayOf(
                0f to accentColor,
                (shimmer - 0.12f).coerceAtLeast(0.01f) to accentColor.copy(alpha = 0.7f),
                shimmer to Color.White,
                (shimmer + 0.12f).coerceAtMost(0.99f) to accentColor.copy(alpha = 0.7f),
                1f to accentColor
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.bg_result),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBg.copy(alpha = 0.72f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(24.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.scale(titleScale.value)
            ) {
                Text(
                    text = if (isDraw) "DRAW" else "VICTORY",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor.copy(alpha = 0.6f),
                        letterSpacing = 8.sp,
                        shadow = Shadow(color = accentColor, blurRadius = 20f)
                    )
                )

                Spacer(Modifier.height(4.dp))

                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isDraw) "NO WINNER" else "🏆",
                        style = TextStyle(
                            fontSize = if (isDraw) 36.sp else 56.sp,
                            color = accentColor.copy(alpha = bloomAlpha),
                            shadow = Shadow(color = accentColor, blurRadius = 120f)
                        )
                    )
                    if (!isDraw) {
                        Text(
                            text = "🏆",
                            style = TextStyle(fontSize = 56.sp)
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, accentColor, Color.Transparent)
                            )
                        )
                )

                Spacer(Modifier.height(12.dp))

                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = winnerName ?: "STALEMATE",
                        style = TextStyle(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = accentColor.copy(alpha = bloomAlpha * 0.8f),
                            letterSpacing = 3.sp,
                            shadow = Shadow(color = accentColor, blurRadius = 100f)
                        )
                    )
                    Text(
                        text = winnerName ?: "STALEMATE",
                        style = TextStyle(
                            brush = titleBrush,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 3.sp,
                            shadow = Shadow(color = accentColor, blurRadius = 14f)
                        )
                    )
                }

                Spacer(Modifier.height(4.dp))

                if (!isDraw) {
                    Text(
                        text = "WINS THE MATCH",
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor.copy(alpha = 0.5f),
                            letterSpacing = 6.sp,
                            shadow = Shadow(color = accentColor, blurRadius = 10f)
                        )
                    )
                }
            }

            AnimatedVisibility(
                visible = showStats,
                enter = fadeIn(tween(600)) + slideInVertically { it / 3 }
            ) {
                val statCardShape = RoundedCornerShape(8.dp)
                val statBg = Brush.verticalGradient(
                    listOf(CardSurface.copy(alpha = 0.7f), CardSurface.copy(alpha = 0.3f))
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            12.dp, statCardShape,
                            spotColor = accentColor.copy(alpha = 0.3f),
                            ambientColor = accentColor.copy(alpha = 0.15f)
                        )
                        .clip(statCardShape)
                        .background(statBg)
                        .border(1.dp, accentColor.copy(alpha = 0.25f), statCardShape)
                        .padding(vertical = 20.dp, horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(3) {
                            Box(
                                Modifier
                                    .size(3.dp)
                                    .background(accentColor, RoundedCornerShape(2.dp))
                            )
                            Spacer(Modifier.width(3.dp))
                        }
                        Text(
                            text = "MATCH SUMMARY",
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor,
                                letterSpacing = 4.sp,
                                shadow = Shadow(color = accentColor, blurRadius = 10f)
                            )
                        )
                        repeat(3) {
                            Spacer(Modifier.width(3.dp))
                            Box(
                                Modifier
                                    .size(3.dp)
                                    .background(accentColor, RoundedCornerShape(2.dp))
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color.Transparent,
                                        accentColor.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    StatRow(label = "ROUNDS", value = "${uiState.roundCount}", accent = accentColor)

                    StatRow(
                        label = "PLAYER 1 HP",
                        value = "${uiState.player1Hp} / 150",
                        accent = if (isVictory) NeonGreen else NeonCyan
                    )

                    StatRow(
                        label = "${uiState.player2Name.uppercase()} HP",
                        value = "${uiState.player2Hp} / 150",
                        accent = if (!isVictory && !isDraw) NeonGreen else NeonCyan
                    )

                    if (uiState.gameMode == GameMode.VS_AI) {
                        StatRow(
                            label = "DIFFICULTY",
                            value = uiState.aiDifficulty.name,
                            accent = when (uiState.aiDifficulty.name) {
                                "EASY" -> NeonGreen
                                "MEDIUM" -> NeonOrange
                                else -> NeonMagenta
                            }
                        )
                    }

                    StatRow(
                        label = "MODE",
                        value = if (uiState.gameMode == GameMode.VS_AI) "VS AI" else "VS PLAYER",
                        accent = NeonCyan
                    )
                }
            }

            AnimatedVisibility(
                visible = showButtons,
                enter = fadeIn(tween(500)) + slideInVertically { it / 2 }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GameButton(label = "PLAY AGAIN", color = NeonMagenta, onClick = onPlayAgain)
                    GameButton(label = "MAIN MENU", color = NeonCyan, onClick = onMainMenu)
                    GameButton(label = "LEADERBOARD", color = NeonOrange, onClick = onLeaderboard)
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun StatRow(label: String, value: String, accent: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.5f),
                letterSpacing = 2.sp
            )
        )
        Text(
            text = value,
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = accent,
                letterSpacing = 1.sp,
                shadow = Shadow(color = accent, blurRadius = 8f)
            )
        )
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
            .height(54.dp)
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
