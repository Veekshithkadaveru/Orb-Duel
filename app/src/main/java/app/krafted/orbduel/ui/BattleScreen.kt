package app.krafted.orbduel.ui

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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.krafted.orbduel.R
import app.krafted.orbduel.game.BattleOutcome
import app.krafted.orbduel.game.TurnPhase
import app.krafted.orbduel.ui.components.HpBar
import app.krafted.orbduel.ui.components.OrbSelectionRow
import app.krafted.orbduel.ui.theme.CardSurface
import app.krafted.orbduel.ui.theme.DarkBg
import app.krafted.orbduel.ui.theme.NeonCyan
import app.krafted.orbduel.ui.theme.NeonGreen
import app.krafted.orbduel.ui.theme.NeonMagenta
import app.krafted.orbduel.ui.theme.NeonOrange
import app.krafted.orbduel.ui.theme.NeonRed
import app.krafted.orbduel.viewmodel.BattleViewModel

private val backgrounds = listOf(
    R.drawable.bg_home,
    R.drawable.bg_battle,
    R.drawable.bg_reveal,
    R.drawable.bg_result,
    R.drawable.bg_leaderboard
)

@Composable
fun BattleScreen(
    viewModel: BattleViewModel,
    onNavigateToReveal: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isRevealPhase = uiState.currentTurn == TurnPhase.REVEAL
    val isResultPhase = uiState.currentTurn == TurnPhase.RESULT
    val isHandoffPhase = uiState.currentTurn == TurnPhase.HANDOFF
    val isPlayer2Select = uiState.currentTurn == TurnPhase.PLAYER2_SELECT

    androidx.compose.runtime.LaunchedEffect(uiState.currentTurn) {
        if (uiState.currentTurn == TurnPhase.REVEAL || uiState.currentTurn == TurnPhase.RESULT) {
            onNavigateToReveal()
        }
    }

    val bgRes = backgrounds[uiState.roundCount % backgrounds.size]

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(bgRes),
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
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ROUND ${uiState.roundCount + 1}",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan,
                    letterSpacing = 4.sp,
                    shadow = Shadow(color = NeonCyan, blurRadius = 14f)
                )
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                HpBar(
                    hp = uiState.player2Hp,
                    label = uiState.player2Name,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(Modifier.height(12.dp))

                val p2Orb = uiState.lastRoundResult?.player2Orb
                if (isResultPhase && p2Orb != null) {
                    Image(
                        painter = painterResource(p2Orb.drawableRes),
                        contentDescription = p2Orb.displayName,
                        modifier = Modifier
                            .size(64.dp)
                            .shadow(
                                8.dp,
                                CircleShape,
                                ambientColor = p2Orb.color,
                                spotColor = p2Orb.color
                            )
                            .border(2.dp, p2Orb.color, CircleShape)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(CardSurface)
                            .border(2.dp, NeonMagenta, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "?",
                            style = TextStyle(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonMagenta,
                                shadow = Shadow(color = NeonMagenta, blurRadius = 12f)
                            )
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, NeonCyan)
                            )
                        )
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "— VS —",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 2.sp,
                        shadow = Shadow(color = NeonCyan, blurRadius = 14f)
                    )
                )
                Spacer(Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(NeonCyan, Color.Transparent)
                            )
                        )
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                val p1Orb = if (isPlayer2Select) {
                    null // Hide Player 1's selection during Player 2's turn
                } else {
                    uiState.player1SelectedOrb ?: uiState.lastRoundResult?.player1Orb
                }
                if (p1Orb != null) {
                    Image(
                        painter = painterResource(p1Orb.drawableRes),
                        contentDescription = p1Orb.displayName,
                        modifier = Modifier
                            .size(64.dp)
                            .shadow(
                                8.dp,
                                CircleShape,
                                ambientColor = p1Orb.color,
                                spotColor = p1Orb.color
                            )
                            .border(2.dp, p1Orb.color, CircleShape)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(CardSurface)
                            .border(2.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "?",
                            style = TextStyle(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan,
                                shadow = Shadow(color = NeonCyan, blurRadius = 12f)
                            )
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                HpBar(
                    hp = uiState.player1Hp,
                    label = uiState.player1Name,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            if (isResultPhase) {
                val winnerText = when (uiState.matchWinner) {
                    app.krafted.orbduel.game.Player.PLAYER1 -> "PLAYER 1 WINS!"
                    app.krafted.orbduel.game.Player.PLAYER2 -> "${uiState.player2Name} WINS!"
                    null -> "DRAW!"
                }
                Text(
                    text = winnerText,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonMagenta,
                        letterSpacing = 3.sp,
                        shadow = Shadow(color = NeonMagenta, blurRadius = 20f)
                    )
                )

                Spacer(Modifier.height(8.dp))

                GameButton(
                    label = "PLAY AGAIN",
                    color = NeonMagenta,
                    enabled = true,
                    onClick = { viewModel.resetMatch() }
                )
            } else if (isHandoffPhase) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "✅ ${uiState.player1Name} is ready!",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreen,
                            shadow = Shadow(color = NeonGreen, blurRadius = 12f)
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Hand the phone to ${uiState.player2Name}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    )
                    Spacer(Modifier.height(32.dp))
                    GameButton(
                        label = "${uiState.player2Name}: PICK ORB",
                        color = NeonCyan,
                        enabled = true,
                        onClick = { viewModel.confirmHandoff() }
                    )
                }
            } else {
                val isP1Turn = uiState.currentTurn == TurnPhase.PLAYER1_SELECT
                val isP2Turn = uiState.currentTurn == TurnPhase.PLAYER2_SELECT

                OrbSelectionRow(
                    selectedOrb = if (isP1Turn) uiState.player1SelectedOrb else uiState.player2SelectedOrb,
                    onOrbSelected = { orb ->
                        if (isP1Turn) viewModel.selectPlayer1Orb(orb)
                        else if (isP2Turn) viewModel.selectPlayer2Orb(orb)
                    },
                    enabled = isP1Turn || isP2Turn
                )

                GameButton(
                    label = "CONFIRM",
                    color = NeonCyan,
                    enabled = (isP1Turn && uiState.player1SelectedOrb != null) ||
                              (isP2Turn && uiState.player2SelectedOrb != null),
                    onClick = {
                        if (isP1Turn) viewModel.confirmPlayer1Selection()
                        else if (isP2Turn) viewModel.confirmPlayer2Selection()
                    }
                )
            }
        }
    }
}

@Composable
private fun GameButton(
    label: String,
    color: Color,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val buttonShape = RoundedCornerShape(4.dp)
    val alpha = if (enabled) 1f else 0.35f
    val displayColor = color.copy(alpha = alpha)
    val gradient = Brush.verticalGradient(
        colors = listOf(displayColor.copy(alpha = 0.30f), displayColor.copy(alpha = 0.06f))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .shadow(
                elevation = 14.dp,
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
                    color = Color.White.copy(alpha = alpha),
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
