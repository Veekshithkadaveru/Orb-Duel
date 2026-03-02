package app.krafted.orbduel.game

class AiOpponent {

    private val orbHistory = mutableMapOf<Element, Int>()

    fun pickOrb(difficulty: Difficulty, playerLastOrb: Element?): Element =
        when (difficulty) {
            Difficulty.EASY   -> pickEasy()
            Difficulty.MEDIUM -> pickMedium(playerLastOrb)
            Difficulty.HARD   -> pickHard()
        }

    fun recordPlayerOrb(orb: Element) {
        orbHistory[orb] = (orbHistory[orb] ?: 0) + 1
    }

    fun reset() = orbHistory.clear()

    private fun pickEasy(): Element = Element.entries.random()

    private fun pickMedium(playerLastOrb: Element?): Element {
        if (playerLastOrb == null || Math.random() < 0.6) return pickEasy()
        return playerLastOrb.weakness()
    }

    private fun pickHard(): Element {
        if (orbHistory.isEmpty()) return pickEasy()
        val mostUsed = orbHistory.maxByOrNull { it.value }!!.key
        return mostUsed.weakness()
    }
}
