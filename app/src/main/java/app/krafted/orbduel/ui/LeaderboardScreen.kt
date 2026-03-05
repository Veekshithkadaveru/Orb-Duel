package app.krafted.orbduel.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import app.krafted.orbduel.data.LeaderboardEntry
import app.krafted.orbduel.ui.components.GameButton
import app.krafted.orbduel.ui.components.NeonDivider
import app.krafted.orbduel.ui.theme.CardSurface
import app.krafted.orbduel.ui.theme.DarkBg
import app.krafted.orbduel.ui.theme.NeonCyan
import app.krafted.orbduel.ui.theme.NeonGreen
import app.krafted.orbduel.ui.theme.NeonMagenta
import app.krafted.orbduel.ui.theme.NeonOrange
import app.krafted.orbduel.ui.theme.GlowYellow
import app.krafted.orbduel.viewmodel.LeaderboardViewModel

@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel,
    onBack: () -> Unit
) {
    val records by viewModel.topRecords.collectAsStateWithLifecycle()

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
                .background(DarkBg.copy(alpha = 0.85f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "🏆 LEADERBOARD 🏆",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = GlowYellow,
                    letterSpacing = 4.sp,
                    shadow = Shadow(color = GlowYellow, blurRadius = 24f)
                )
            )

            Spacer(Modifier.height(16.dp))

            NeonDivider(color = NeonOrange, widthFraction = 0.8f)

            Spacer(Modifier.height(24.dp))

            if (records.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "NO MATCHES PLAYED YET",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.4f),
                            letterSpacing = 2.sp
                        )
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(records) { index, entry ->
                        RecordItem(rank = index + 1, entry = entry)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            GameButton(label = "BACK", color = NeonMagenta, onClick = onBack)
        }
    }
}

@Composable
private fun RecordItem(rank: Int, entry: LeaderboardEntry) {
    val shape = RoundedCornerShape(8.dp)

    val accentColor = when (rank) {
        1 -> GlowYellow
        2 -> NeonCyan
        3 -> NeonGreen
        else -> NeonMagenta
    }

    val bgGradient = Brush.verticalGradient(
        listOf(
            CardSurface.copy(alpha = 0.6f),
            CardSurface.copy(alpha = 0.2f)
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (rank <= 3) 8.dp else 4.dp,
                shape = shape,
                spotColor = accentColor.copy(alpha = 0.4f),
                ambientColor = accentColor.copy(alpha = 0.1f)
            )
            .clip(shape)
            .background(bgGradient)
            .border(
                width = if (rank == 1) 1.5.dp else 1.dp,
                color = accentColor.copy(alpha = 0.3f),
                shape = shape
            )
            .padding(vertical = 16.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "#$rank",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = accentColor,
                shadow = Shadow(color = accentColor, blurRadius = 12f)
            ),
            modifier = Modifier.width(44.dp)
        )

        Spacer(Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            val winnerText = entry.playerName.uppercase()

            Text(
                text = winnerText,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            )

            Spacer(Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${entry.wins} WINS",
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        shadow = Shadow(color = accentColor, blurRadius = 8f),
                        letterSpacing = 1.sp
                    )
                )

                Spacer(Modifier.width(8.dp))
                Text(text = "•", color = Color.White.copy(alpha = 0.3f), fontSize = 10.sp)
                Spacer(Modifier.width(8.dp))

                if (entry.hpKnockouts > 0) {
                    Text(
                        text = "${entry.hpKnockouts} KO",
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreen,
                            shadow = Shadow(color = NeonGreen, blurRadius = 8f),
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = "•", color = Color.White.copy(alpha = 0.3f), fontSize = 10.sp)
                    Spacer(Modifier.width(8.dp))
                }

                val survivedWins = entry.wins - entry.hpKnockouts
                Text(
                    text = if (survivedWins > 0) "Rd ${entry.fastestRound}" else "Best: Rd ${entry.fastestRound}",
                    style = TextStyle(
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )
                )
            }
        }

        // HP
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "PEAK HP",
                style = TextStyle(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.4f),
                    letterSpacing = 1.sp
                )
            )
            Text(
                text = "${entry.highestHp}",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonGreen,
                    shadow = Shadow(color = NeonGreen, blurRadius = 10f)
                )
            )
        }
    }
}
