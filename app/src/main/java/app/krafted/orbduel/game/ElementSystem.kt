package app.krafted.orbduel.game

enum class Element(val displayName: String) {
    PSYCHIC ("Psychic"),
    FIRE    ("Fire"),
    POISON  ("Poison"),
    COSMIC  ("Cosmic"),
    ASTEROID("Asteroid"),
    WATER   ("Water"),
    NEBULA  ("Nebula")
}

fun Element.beats(): Element = when (this) {
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
    defender == attacker.beats() -> BattleOutcome.WIN
    attacker == defender.beats() -> BattleOutcome.LOSE
    else                         -> BattleOutcome.DRAW
}

object DamageValues {
    const val WIN_DAMAGE  = 30
    const val LOSE_DAMAGE = 15
    const val DRAW_DAMAGE = 0
}
