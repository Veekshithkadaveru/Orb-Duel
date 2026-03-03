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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import app.krafted.orbduel.game.Difficulty
import app.krafted.orbduel.ui.theme.CardSurface
import app.krafted.orbduel.ui.theme.DarkBg
import app.krafted.orbduel.ui.theme.NeonCyan
import app.krafted.orbduel.ui.theme.NeonGreen
import app.krafted.orbduel.ui.theme.NeonMagenta
import app.krafted.orbduel.ui.theme.NeonOrange
import app.krafted.orbduel.ui.theme.NeonRed

@Composable
fun ModeSelectScreen(
    mode: String,
    onDifficultySelected: (Difficulty) -> Unit,
    onStartPlayerGame: (String) -> Unit,
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
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (mode) {
                "VS_AI" -> AiModeContent(onDifficultySelected)
                else -> PlayerModeContent(onStartPlayerGame)
            }

            Spacer(Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, NeonMagenta, Color.Transparent)
                        )
                    )
            )

            Spacer(Modifier.height(24.dp))

            SelectButton(label = "BACK", color = NeonMagenta.copy(alpha = 0.6f), onClick = onBack)
        }
    }
}

@Composable
private fun AiModeContent(onDifficultySelected: (Difficulty) -> Unit) {
    Text(
        text = "SELECT DIFFICULTY",
        style = TextStyle(
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 4.sp,
            shadow = Shadow(color = NeonMagenta, blurRadius = 20f)
        )
    )

    Spacer(Modifier.height(40.dp))

    SelectButton(label = "EASY", color = NeonGreen) {
        onDifficultySelected(Difficulty.EASY)
    }
    Spacer(Modifier.height(14.dp))
    SelectButton(label = "MEDIUM", color = NeonOrange) {
        onDifficultySelected(Difficulty.MEDIUM)
    }
    Spacer(Modifier.height(14.dp))
    SelectButton(label = "HARD", color = NeonRed) {
        onDifficultySelected(Difficulty.HARD)
    }
}

@Composable
private fun PlayerModeContent(onStartPlayerGame: (String) -> Unit) {
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
        value = player2Name,
        onValueChange = { player2Name = it },
        label = { Text("Player 2 Name") },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonCyan,
            unfocusedBorderColor = NeonCyan.copy(alpha = 0.4f),
            focusedLabelColor = NeonCyan,
            unfocusedLabelColor = NeonCyan.copy(alpha = 0.6f),
            cursorColor = NeonCyan,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = CardSurface,
            unfocusedContainerColor = CardSurface
        ),
        textStyle = TextStyle(fontSize = 16.sp, letterSpacing = 1.sp),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(24.dp))

    SelectButton(
        label = "START GAME",
        color = NeonCyan,
        enabled = player2Name.isNotBlank()
    ) {
        onStartPlayerGame(player2Name.trim())
    }
}

@Composable
private fun SelectButton(
    label: String,
    color: Color,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val buttonShape = RoundedCornerShape(4.dp)
    val displayColor = if (enabled) color else color.copy(alpha = 0.3f)
    val gradient = Brush.verticalGradient(
        colors = listOf(displayColor.copy(alpha = 0.30f), displayColor.copy(alpha = 0.06f))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .shadow(
                elevation = if (enabled) 14.dp else 4.dp,
                shape = buttonShape,
                spotColor = displayColor,
                ambientColor = displayColor.copy(alpha = 0.45f)
            )
            .clip(buttonShape)
            .background(gradient)
            .border(1.5.dp, displayColor, buttonShape)
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier),
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
                    color = if (enabled) Color.White else Color.White.copy(alpha = 0.4f),
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
