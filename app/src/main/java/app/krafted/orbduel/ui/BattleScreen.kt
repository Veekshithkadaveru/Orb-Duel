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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.Text
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.krafted.orbduel.R
import app.krafted.orbduel.game.Element
import app.krafted.orbduel.game.Player
import app.krafted.orbduel.game.TurnPhase
import app.krafted.orbduel.game.beats
import app.krafted.orbduel.ui.components.GameButton
import app.krafted.orbduel.ui.components.HpBar
import app.krafted.orbduel.ui.components.NeonDivider
import app.krafted.orbduel.ui.components.OrbDisplay
import app.krafted.orbduel.ui.components.OrbSelectionRow
import app.krafted.orbduel.ui.theme.CardSurface
import app.krafted.orbduel.ui.theme.DarkBg
import app.krafted.orbduel.ui.theme.NeonCyan
import app.krafted.orbduel.ui.theme.NeonGreen
import app.krafted.orbduel.ui.theme.NeonMagenta
import app.krafted.orbduel.ui.theme.NeonOrange
import app.krafted.orbduel.ui.theme.NeonRed
import app.krafted.orbduel.ui.theme.color
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

    LaunchedEffect(uiState.currentTurn) {
        if (uiState.currentTurn == TurnPhase.REVEAL || uiState.currentTurn == TurnPhase.RESULT) {
            onNavigateToReveal()
        }
    }

    var showRules by remember { mutableStateOf(false) }

    val bgRes = backgrounds[uiState.roundCount % backgrounds.size]

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val isCompact = screenHeight < 640.dp
    val hPad = if (isCompact) 16.dp else 24.dp
    val orbSize = if (isCompact) 52.dp else 64.dp

    if (showRules) {
        RulesDialog(onDismiss = { showRules = false })
    }

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
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = hPad, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top: Round header + rules button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.size(36.dp))

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

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CardSurface)
                        .border(1.dp, NeonCyan.copy(alpha = 0.5f), CircleShape)
                        .clickable { showRules = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "?",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            shadow = Shadow(color = NeonCyan, blurRadius = 10f)
                        )
                    )
                }
            }

            // Middle: flexible area for game content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Player 2 section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HpBar(
                        hp = uiState.player2Hp,
                        label = uiState.player2Name,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(Modifier.height(if (isCompact) 8.dp else 12.dp))

                    val p2Orb = if (isResultPhase) uiState.lastRoundResult?.player2Orb else null
                    OrbDisplay(element = p2Orb, placeholderColor = NeonMagenta, size = orbSize)
                }

                // VS divider
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
                            fontSize = if (isCompact) 14.sp else 16.sp,
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

                // Player 1 section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val p1Orb = if (uiState.currentTurn == TurnPhase.PLAYER2_SELECT || uiState.currentTurn == TurnPhase.HANDOFF) {
                        null
                    } else {
                        uiState.player1SelectedOrb ?: uiState.lastRoundResult?.player1Orb
                    }
                    OrbDisplay(element = p1Orb, placeholderColor = NeonCyan, size = orbSize)

                    Spacer(Modifier.height(if (isCompact) 8.dp else 12.dp))

                    HpBar(
                        hp = uiState.player1Hp,
                        label = uiState.player1Name,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            // Bottom: action area (confirm button, handoff, result)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (isCompact) 16.dp else 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isResultPhase) {
                    val winnerText = when (uiState.matchWinner) {
                        Player.PLAYER1 -> "PLAYER 1 WINS!"
                        Player.PLAYER2 -> "${uiState.player2Name} WINS!"
                        null -> "DRAW!"
                    }
                    Text(
                        text = winnerText,
                        style = TextStyle(
                            fontSize = if (isCompact) 18.sp else 22.sp,
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
                    Text(
                        text = "${uiState.player1Name} is ready!",
                        style = TextStyle(
                            fontSize = if (isCompact) 18.sp else 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreen,
                            shadow = Shadow(color = NeonGreen, blurRadius = 12f)
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Hand the phone to ${uiState.player2Name}",
                        style = TextStyle(
                            fontSize = if (isCompact) 14.sp else 16.sp,
                            color = Color.White
                        )
                    )
                    Spacer(Modifier.height(24.dp))
                    GameButton(
                        label = "${uiState.player2Name}: PICK ORB",
                        color = NeonCyan,
                        enabled = true,
                        onClick = { viewModel.confirmHandoff() }
                    )
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

                    Spacer(Modifier.height(12.dp))

                    val buttonLabel = if (isP1Turn) "CONFIRM ${uiState.player1Name}'s ORB" else "CONFIRM ${uiState.player2Name}'s ORB"
                    val isPlayerSelected = (isP1Turn && uiState.player1SelectedOrb != null) ||
                                           (isP2Turn && uiState.player2SelectedOrb != null)

                    GameButton(
                        label = buttonLabel,
                        color = NeonCyan,
                        enabled = isPlayerSelected,
                        infinitePulse = true,
                        onClick = {
                            if (isP1Turn) viewModel.confirmPlayer1Selection()
                            else if (isP2Turn) viewModel.confirmPlayer2Selection()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RulesDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        val shape = RoundedCornerShape(12.dp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(DarkBg)
                .border(1.dp, NeonCyan.copy(alpha = 0.4f), shape)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ORB RULES",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan,
                    letterSpacing = 4.sp,
                    shadow = Shadow(color = NeonCyan, blurRadius = 14f)
                )
            )

            Spacer(Modifier.height(16.dp))

            NeonDivider(color = NeonCyan, widthFraction = 0.6f)

            Spacer(Modifier.height(16.dp))

            Element.entries.forEach { element ->
                val beats = element.beats()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = element.displayName.uppercase(),
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = element.color,
                            shadow = Shadow(color = element.color, blurRadius = 8f)
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "beats",
                        style = TextStyle(
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = beats.displayName.uppercase(),
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = beats.color,
                            shadow = Shadow(color = beats.color, blurRadius = 8f)
                        ),
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.End
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            NeonDivider(color = NeonOrange, widthFraction = 0.6f)

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Neutral matchups deal no damage",
                style = TextStyle(
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.4f),
                    letterSpacing = 1.sp
                )
            )

            Spacer(Modifier.height(16.dp))

            GameButton(label = "GOT IT", color = NeonCyan, onClick = onDismiss)
        }
    }
}
