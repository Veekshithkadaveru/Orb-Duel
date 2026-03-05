package app.krafted.orbduel.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.orbduel.game.Element
import app.krafted.orbduel.ui.theme.CardSurface
import app.krafted.orbduel.ui.theme.color
import app.krafted.orbduel.ui.theme.drawableRes

@Composable
fun OrbDisplay(element: Element?, placeholderColor: Color, size: Dp = 64.dp) {
    if (element != null) {
        Image(
            painter = painterResource(element.drawableRes),
            contentDescription = element.displayName,
            modifier = Modifier
                .size(size)
                .shadow(
                    8.dp,
                    CircleShape,
                    ambientColor = element.color,
                    spotColor = element.color
                )
                .border(2.dp, element.color, CircleShape)
                .clip(CircleShape)
        )
    } else {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(CardSurface)
                .border(2.dp, placeholderColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "?",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = placeholderColor,
                    shadow = Shadow(color = placeholderColor, blurRadius = 12f)
                )
            )
        }
    }
}
