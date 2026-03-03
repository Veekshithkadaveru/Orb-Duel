package app.krafted.orbduel.game

import androidx.compose.ui.graphics.Color
import app.krafted.orbduel.R

enum class Element(
    val displayName: String,
    val color: Color,
    val drawableRes: Int
) {
    PSYCHIC ("Psychic",  Color(0xFFFF66FF), R.drawable.orb_psychic),
    FIRE    ("Fire",     Color(0xFFFF6600), R.drawable.orb_fire),
    POISON  ("Poison",   Color(0xFF44FF44), R.drawable.orb_poison),
    COSMIC  ("Cosmic",   Color(0xFF9900FF), R.drawable.orb_cosmic),
    ASTEROID("Asteroid", Color(0xFFFFD700), R.drawable.orb_asteroid),
    WATER   ("Water",    Color(0xFF00CCFF), R.drawable.orb_water),
    NEBULA  ("Nebula",   Color(0xFF66AAFF), R.drawable.orb_nebula)
}

fun Element.weakness(): Element = when (this) {
    Element.PSYCHIC  -> Element.COSMIC
    Element.COSMIC   -> Element.ASTEROID
    Element.ASTEROID -> Element.WATER
    Element.WATER    -> Element.FIRE
    Element.FIRE     -> Element.POISON
    Element.POISON   -> Element.NEBULA
    Element.NEBULA   -> Element.PSYCHIC
}

enum class BattleOutcome { WIN, LOSE, DRAW }

fun resolveBattle(attacker: Element, defender: Element): BattleOutcome = when {
    attacker == defender            -> BattleOutcome.DRAW
    defender == attacker.weakness() -> BattleOutcome.WIN
    attacker == defender.weakness() -> BattleOutcome.LOSE
    else                            -> BattleOutcome.DRAW
}

object DamageValues {
    const val WIN_DAMAGE  = 30
    const val LOSE_DAMAGE = 15
    const val DRAW_DAMAGE = 0
}
