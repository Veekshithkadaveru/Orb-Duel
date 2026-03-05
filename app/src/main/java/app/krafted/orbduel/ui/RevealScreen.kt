package app.krafted.orbduel.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.krafted.orbduel.R
import app.krafted.orbduel.game.BattleOutcome
import app.krafted.orbduel.game.GameMode
import app.krafted.orbduel.game.MAX_ROUNDS
import app.krafted.orbduel.game.TurnPhase
import app.krafted.orbduel.ui.components.GameButton
import app.krafted.orbduel.ui.theme.DarkBg
import app.krafted.orbduel.ui.theme.NeonCyan
import app.krafted.orbduel.ui.theme.NeonGreen
import app.krafted.orbduel.ui.theme.NeonMagenta
import app.krafted.orbduel.ui.theme.NeonOrange
import app.krafted.orbduel.ui.theme.NeonRed
import app.krafted.orbduel.ui.theme.drawableRes
import app.krafted.orbduel.viewmodel.BattleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun RevealScreen(
    viewModel: BattleViewModel,
    onNextRound: () -> Unit,
    onMatchOver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val result = uiState.lastRoundResult ?: return

    val config = LocalConfiguration.current
    val screenWidthDp = config.screenWidthDp.dp
    val screenHeightDp = config.screenHeightDp.dp
    val screenWidthPx = with(LocalDensity.current) { screenWidthDp.toPx() }
    val isCompact = screenHeightDp < 640.dp

    // Scale sizes relative to screen
    val orbSize = min(screenWidthDp * 0.25f, 100.dp)
    val glowSize = min(screenWidthDp * 0.75f, 300.dp)
    val orbSpreadPx = screenWidthPx * 0.15f
    val bannerOffsetY = -(screenHeightDp * 0.13f)
    val damageFloatTarget = -(screenHeightDp.value * 0.15f)
    val bottomPad = if (isCompact) 32.dp else 64.dp
    val hPad = if (isCompact) 16.dp else 32.dp

    // Animation States
    val p1OffsetX = remember { Animatable(-screenWidthPx) }
    val p2OffsetX = remember { Animatable(screenWidthPx) }
    val clashScale = remember { Animatable(1f) }
    val glowAlpha = remember { Animatable(0f) }
    val damageOffsetY = remember { Animatable(0f) }
    val damageAlpha = remember { Animatable(0f) }

    var showDamage by remember { mutableStateOf(false) }
    var showBanner by remember { mutableStateOf(false) }
    var showNextButton by remember { mutableStateOf(false) }

    LaunchedEffect(result) {

        launch {
            p1OffsetX.animateTo(
                targetValue = -orbSpreadPx,
                animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessLow)
            )
        }
        launch {
            p2OffsetX.animateTo(
                targetValue = orbSpreadPx,
                animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessLow)
            )
        }

        delay(800)

        launch {
            glowAlpha.animateTo(1f, tween(150))
            glowAlpha.animateTo(0f, tween(300))
        }
        clashScale.animateTo(1.6f, spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessHigh))
        clashScale.animateTo(1.0f, tween(200))

        delay(250)

        when (result.player1Outcome) {
            BattleOutcome.WIN -> {
                launch { p2OffsetX.animateTo(screenWidthPx, tween(500)) }
                launch { p1OffsetX.animateTo(0f, tween(500)) }
                showDamage = true
                showBanner = true
            }
            BattleOutcome.LOSE -> {
                launch { p1OffsetX.animateTo(-screenWidthPx, tween(500)) }
                launch { p2OffsetX.animateTo(0f, tween(500)) }
                showDamage = true
                showBanner = true
            }
            BattleOutcome.DRAW -> {
                showBanner = true
            }
        }
        if (result.player1Outcome != BattleOutcome.DRAW) {
            launch {
                damageAlpha.animateTo(1f, tween(200))
                damageOffsetY.animateTo(damageFloatTarget, tween(800, easing = FastOutLinearInEasing))
                damageAlpha.animateTo(0f, tween(200))
            }
        }

        delay(600)
        showNextButton = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_reveal),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBg.copy(alpha = 0.75f))
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(glowSize)
                .alpha(glowAlpha.value)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.White, Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset { IntOffset(p1OffsetX.value.roundToInt(), 0) }
                .scale(clashScale.value)
        ) {
            Image(
                painter = painterResource(result.player1Orb.drawableRes),
                contentDescription = result.player1Orb.displayName,
                modifier = Modifier.size(orbSize)
            )

            if (showDamage && result.player1Outcome == BattleOutcome.LOSE) {
                Text(
                    text = "-${result.damageTakenByP1} HP",
                    style = TextStyle(
                        fontSize = if (isCompact) 22.sp else 28.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonRed,
                        shadow = Shadow(color = NeonRed, blurRadius = 14f)
                    ),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = damageOffsetY.value.dp)
                        .alpha(damageAlpha.value)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset { IntOffset(p2OffsetX.value.roundToInt(), 0) }
                .scale(clashScale.value)
        ) {
            Image(
                painter = painterResource(result.player2Orb.drawableRes),
                contentDescription = result.player2Orb.displayName,
                modifier = Modifier.size(orbSize)
            )

            if (showDamage && result.player1Outcome == BattleOutcome.WIN) {
                Text(
                    text = "-${result.damageTakenByP2} HP",
                    style = TextStyle(
                        fontSize = if (isCompact) 22.sp else 28.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonCyan,
                        shadow = Shadow(color = NeonCyan, blurRadius = 14f)
                    ),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = damageOffsetY.value.dp)
                        .alpha(damageAlpha.value)
                )
            }
        }

        AnimatedVisibility(
            visible = showBanner,
            enter = fadeIn(tween(500)),
            modifier = Modifier.align(Alignment.Center).offset(y = bannerOffsetY)
        ) {
            val isGameOver = uiState.matchWinner != null || uiState.roundCount >= MAX_ROUNDS

            val bannerText = if (isGameOver) {
                "MATCH OVER!"
            } else if (uiState.gameMode == GameMode.VS_PLAYER) {
                when (result.player1Outcome) {
                    BattleOutcome.WIN -> "${uiState.player1Name.uppercase()} WINS!"
                    BattleOutcome.LOSE -> "${uiState.player2Name.uppercase()} WINS!"
                    else -> "DRAW!"
                }
            } else {
                when (result.player1Outcome) {
                    BattleOutcome.WIN -> "YOU WIN!"
                    BattleOutcome.LOSE -> "YOU LOSE!"
                    else -> "DRAW!"
                }
            }

            val bannerColor = when {
                isGameOver -> NeonMagenta
                result.player1Outcome == BattleOutcome.WIN -> NeonGreen
                result.player1Outcome == BattleOutcome.LOSE -> NeonRed
                else -> NeonOrange
            }

            Text(
                text = bannerText,
                style = TextStyle(
                    fontSize = if (isCompact) 26.sp else 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = bannerColor,
                    letterSpacing = 4.sp,
                    shadow = Shadow(color = bannerColor, blurRadius = 20f)
                )
            )
        }

        AnimatedVisibility(
            visible = showNextButton,
            enter = fadeIn(tween(500)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(bottom = bottomPad, start = hPad, end = hPad)
        ) {
            val isGameOver = uiState.matchWinner != null || uiState.roundCount >= MAX_ROUNDS
            val buttonText = if (isGameOver) "VIEW RESULTS" else "NEXT ROUND"
            val buttonColor = if (isGameOver) NeonMagenta else NeonCyan

            GameButton(
                label = buttonText,
                color = buttonColor,
                enabled = true,
                onClick = {
                    if (isGameOver) {
                        onMatchOver()
                    } else {
                        viewModel.nextRound()
                        onNextRound()
                    }
                }
            )
        }
    }
}
