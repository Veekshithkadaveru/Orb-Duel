package app.krafted.orbduel.ui.theme

import androidx.compose.ui.graphics.Color
import app.krafted.orbduel.R
import app.krafted.orbduel.game.Element

val Element.color: Color
    get() = when (this) {
        Element.PSYCHIC  -> Color(0xFFFF66FF)
        Element.FIRE     -> Color(0xFFFF6600)
        Element.POISON   -> Color(0xFF44FF44)
        Element.COSMIC   -> Color(0xFF9900FF)
        Element.ASTEROID -> Color(0xFFFFD700)
        Element.WATER    -> Color(0xFF00CCFF)
        Element.NEBULA   -> Color(0xFF66AAFF)
    }

val Element.drawableRes: Int
    get() = when (this) {
        Element.PSYCHIC  -> R.drawable.orb_psychic
        Element.FIRE     -> R.drawable.orb_fire
        Element.POISON   -> R.drawable.orb_poison
        Element.COSMIC   -> R.drawable.orb_cosmic
        Element.ASTEROID -> R.drawable.orb_asteroid
        Element.WATER    -> R.drawable.orb_water
        Element.NEBULA   -> R.drawable.orb_nebula
    }
