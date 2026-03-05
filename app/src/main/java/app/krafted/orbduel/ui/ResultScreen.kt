package app.krafted.orbduel.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.krafted.orbduel.R
import app.krafted.orbduel.game.GameMode
import app.krafted.orbduel.game.Player
import app.krafted.orbduel.ui.components.DecorativeDotLabel
import app.krafted.orbduel.ui.components.GameButton
import app.krafted.orbduel.ui.components.NeonDivider
import app.krafted.orbduel.ui.components.rememberBloomAlpha
import app.krafted.orbduel.ui.components.rememberShimmerBrush
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
        Player.PLAYER1 -> uiState.player1Name.uppercase()
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

    val bloomAlpha = rememberBloomAlpha(
        minAlpha = 0.06f,
        maxAlpha = 0.22f,
        durationMs = 1600,
        label = "result"
    )

    val titleBrush = rememberShimmerBrush(
        accentColor = accentColor,
        shimmerDurationMs = 3000,
        label = "result"
    )

    val titleScale = remember { Animatable(0f) }
    var showStats by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }

    val config = LocalConfiguration.current
    val isCompact = config.screenHeightDp < 640
    val isNarrow = config.screenWidthDp < 360
    val hPad = if (isNarrow) 20.dp else 28.dp
    val vPad = if (isCompact) 20.dp else 32.dp
    val trophySize = if (isCompact) 42.sp else 56.sp
    val winnerNameSize = if (isNarrow) 26.sp else 32.sp
    val buttonHeight = if (isCompact) 48.dp else 54.dp

    LaunchedEffect(Unit) {
        titleScale.animateTo(1f, spring(dampingRatio = 0.55f, stiffness = 120f))
        delay(300)
        showStats = true
        delay(400)
        showButtons = true
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
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = hPad, vertical = vPad),
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
                            fontSize = if (isDraw) (trophySize * 0.64f) else trophySize,
                            color = accentColor.copy(alpha = bloomAlpha),
                            shadow = Shadow(color = accentColor, blurRadius = 120f)
                        )
                    )
                    if (!isDraw) {
                        Text(
                            text = "🏆",
                            style = TextStyle(fontSize = trophySize)
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                NeonDivider(color = accentColor, widthFraction = 0.65f)

                Spacer(Modifier.height(12.dp))

                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = winnerName ?: "STALEMATE",
                        style = TextStyle(
                            fontSize = winnerNameSize,
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
                            fontSize = winnerNameSize,
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
                        DecorativeDotLabel(
                            text = "MATCH SUMMARY",
                            color = accentColor,
                            dotSize = 3.dp,
                            fontSize = 10,
                            letterSpacing = 4
                        )
                    }

                    NeonDivider(color = accentColor.copy(alpha = 0.3f))

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
                    GameButton(label = "PLAY AGAIN", color = NeonMagenta, height = buttonHeight, onClick = onPlayAgain)
                    GameButton(label = "MAIN MENU", color = NeonCyan, height = buttonHeight, onClick = onMainMenu)
                    GameButton(label = "LEADERBOARD", color = NeonOrange, height = buttonHeight, onClick = onLeaderboard)
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
