package app.krafted.orbduel.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.orbduel.R
import app.krafted.orbduel.game.Difficulty
import app.krafted.orbduel.ui.components.GameButton
import app.krafted.orbduel.ui.components.NeonDivider
import app.krafted.orbduel.ui.theme.CardSurface
import app.krafted.orbduel.ui.theme.DarkBg
import app.krafted.orbduel.ui.theme.NeonCyan
import app.krafted.orbduel.ui.theme.NeonGreen
import app.krafted.orbduel.ui.theme.NeonMagenta
import app.krafted.orbduel.ui.theme.NeonOrange
import app.krafted.orbduel.ui.theme.NeonRed

@Composable
private fun neonTextFieldColors(color: Color): TextFieldColors =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = color,
        unfocusedBorderColor = color.copy(alpha = 0.4f),
        focusedLabelColor = color,
        unfocusedLabelColor = color.copy(alpha = 0.6f),
        cursorColor = color,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedContainerColor = CardSurface,
        unfocusedContainerColor = CardSurface
    )

@Composable
fun ModeSelectScreen(
    mode: String,
    onDifficultySelected: (Difficulty, String) -> Unit,
    onStartPlayerGame: (String, String) -> Unit,
    onBack: () -> Unit
) {
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
                .background(DarkBg.copy(alpha = 0.65f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (mode) {
                "VS_AI" -> AiModeContent(onDifficultySelected)
                else -> PlayerModeContent(onStartPlayerGame)
            }

            Spacer(Modifier.height(32.dp))

            NeonDivider(color = NeonMagenta, widthFraction = 0.72f)

            Spacer(Modifier.height(24.dp))

            GameButton(label = "BACK", color = NeonMagenta.copy(alpha = 0.6f), onClick = onBack)
        }
    }
}

@Composable
private fun AiModeContent(onDifficultySelected: (Difficulty, String) -> Unit) {
    var playerName by remember { mutableStateOf("") }

    Text(
        text = "PLAYER VS AI",
        style = TextStyle(
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 4.sp,
            shadow = Shadow(color = NeonMagenta, blurRadius = 20f)
        )
    )

    Spacer(Modifier.height(40.dp))

    OutlinedTextField(
        value = playerName,
        onValueChange = { playerName = it },
        label = { Text("Your Name") },
        singleLine = true,
        colors = neonTextFieldColors(NeonMagenta),
        textStyle = TextStyle(fontSize = 16.sp, letterSpacing = 1.sp),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(24.dp))

    GameButton(label = "EASY", color = NeonGreen, enabled = playerName.isNotBlank()) {
        onDifficultySelected(Difficulty.EASY, playerName.trim())
    }
    Spacer(Modifier.height(14.dp))
    GameButton(label = "MEDIUM", color = NeonOrange, enabled = playerName.isNotBlank()) {
        onDifficultySelected(Difficulty.MEDIUM, playerName.trim())
    }
    Spacer(Modifier.height(14.dp))
    GameButton(label = "HARD", color = NeonRed, enabled = playerName.isNotBlank()) {
        onDifficultySelected(Difficulty.HARD, playerName.trim())
    }
}

@Composable
private fun PlayerModeContent(onStartPlayerGame: (String, String) -> Unit) {
    var player1Name by remember { mutableStateOf("") }
    var player2Name by remember { mutableStateOf("") }

    Text(
        text = "PLAYER VS PLAYER",
        style = TextStyle(
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 4.sp,
            shadow = Shadow(color = NeonCyan, blurRadius = 20f)
        )
    )

    Spacer(Modifier.height(40.dp))

    OutlinedTextField(
        value = player1Name,
        onValueChange = { player1Name = it },
        label = { Text("Player 1 Name") },
        singleLine = true,
        colors = neonTextFieldColors(NeonCyan),
        textStyle = TextStyle(fontSize = 16.sp, letterSpacing = 1.sp),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(16.dp))

    OutlinedTextField(
        value = player2Name,
        onValueChange = { player2Name = it },
        label = { Text("Player 2 Name") },
        singleLine = true,
        colors = neonTextFieldColors(NeonCyan),
        textStyle = TextStyle(fontSize = 16.sp, letterSpacing = 1.sp),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(24.dp))

    GameButton(
        label = "START GAME",
        color = NeonCyan,
        enabled = player1Name.isNotBlank() && player2Name.isNotBlank()
    ) {
        onStartPlayerGame(player1Name.trim(), player2Name.trim())
    }
}
