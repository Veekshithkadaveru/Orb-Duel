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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.orbduel.R
import app.krafted.orbduel.ui.components.DecorativeDotLabel
import app.krafted.orbduel.ui.components.GameButton
import app.krafted.orbduel.ui.components.NeonDivider
import app.krafted.orbduel.ui.components.rememberBloomAlpha
import app.krafted.orbduel.ui.components.rememberShimmerBrush
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
    val bloomAlpha = rememberBloomAlpha(label = "title")
    val titleBrush = rememberShimmerBrush(
        accentColor = NeonMagenta,
        secondaryColor = NeonCyan,
        label = "title"
    )

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val isCompact = screenWidth < 360.dp
    val titleSize = if (isCompact) 42.sp else 56.sp
    val hPad = if (isCompact) 20.dp else 28.dp

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
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = hPad),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            NeonDivider(
                colors = listOf(Color.Transparent, NeonCyan, NeonMagenta, Color.Transparent),
                widthFraction = 0.75f
            )

            Spacer(Modifier.height(20.dp))

            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "ORB DUEL",
                    softWrap = false,
                    style = TextStyle(
                        fontSize = titleSize,
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
                        fontSize = titleSize,
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
                        fontSize = titleSize,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 4.sp,
                        shadow = Shadow(color = NeonMagenta, blurRadius = 20f)
                    )
                )
            }

            Spacer(Modifier.height(10.dp))

            DecorativeDotLabel(text = "COSMIC BATTLE ARENA", color = NeonCyan)

            Spacer(Modifier.height(16.dp))

            NeonDivider(
                colors = listOf(Color.Transparent, NeonMagenta, NeonCyan, NeonMagenta, Color.Transparent),
                widthFraction = 0.72f
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
