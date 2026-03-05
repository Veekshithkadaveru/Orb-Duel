package app.krafted.orbduel.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NeonDivider(color: Color, widthFraction: Float = 1f) {
    Box(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(1.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(Color.Transparent, color, Color.Transparent)
                )
            )
    )
}

@Composable
fun NeonDivider(colors: List<Color>, widthFraction: Float = 1f) {
    Box(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(1.dp)
            .background(
                Brush.horizontalGradient(colors)
            )
    )
}

@Composable
fun DecorativeDotLabel(
    text: String,
    color: Color,
    dotSize: Dp = 4.dp,
    fontSize: Int = 10,
    letterSpacing: Int = 4
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(3) {
            Box(
                Modifier
                    .size(dotSize)
                    .background(color, RoundedCornerShape(dotSize / 2))
            )
            Spacer(Modifier.width(dotSize))
        }
        Text(
            text = text,
            style = TextStyle(
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                letterSpacing = letterSpacing.sp,
                shadow = Shadow(color = color, blurRadius = if (dotSize >= 4.dp) 14f else 10f)
            )
        )
        repeat(3) {
            Spacer(Modifier.width(dotSize))
            Box(
                Modifier
                    .size(dotSize)
                    .background(color, RoundedCornerShape(dotSize / 2))
            )
        }
    }
}
