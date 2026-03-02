package app.krafted.orbduel.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NeonColorScheme = darkColorScheme(
    primary          = NeonMagenta,
    onPrimary        = Color.Black,
    secondary        = NeonCyan,
    onSecondary      = Color.Black,
    tertiary         = NeonOrange,
    onTertiary       = Color.Black,
    background       = DarkBg,
    onBackground     = Color.White,
    surface          = CardSurface,
    onSurface        = Color.White,
    surfaceVariant   = DeepPurple,
    onSurfaceVariant = Color.White,
    error            = NeonRed,
    onError          = Color.Black,
)

@Composable
fun OrbDuelTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NeonColorScheme,
        typography  = Typography,
        content     = content
    )
}
