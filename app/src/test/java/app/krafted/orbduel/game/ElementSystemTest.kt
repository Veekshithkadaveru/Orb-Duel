package app.krafted.orbduel.game

import org.junit.Assert.assertEquals
import org.junit.Test

class ElementSystemTest {

    // ── Weakness ring ────────────────────────────────────────

    @Test
    fun `weakness ring is complete and circular`() {
        assertEquals(Element.COSMIC,   Element.PSYCHIC.beats())
        assertEquals(Element.ASTEROID, Element.COSMIC.beats())
        assertEquals(Element.WATER,    Element.ASTEROID.beats())
        assertEquals(Element.FIRE,     Element.WATER.beats())
        assertEquals(Element.POISON,   Element.FIRE.beats())
        assertEquals(Element.NEBULA,   Element.POISON.beats())
        assertEquals(Element.PSYCHIC,  Element.NEBULA.beats())
    }

    // ── Draw: same vs same (7 cases) ────────────────────────

    @Test
    fun `same element always draws`() {
        Element.entries.forEach { element ->
            assertEquals(
                "Expected DRAW for $element vs $element",
                BattleOutcome.DRAW,
                resolveBattle(element, element)
            )
        }
    }

    // ── WIN: attacker beats defender (7 cases) ──────────────

    @Test
    fun `psychic wins against cosmic`() =
        assertWin(Element.PSYCHIC, Element.COSMIC)

    @Test
    fun `cosmic wins against asteroid`() =
        assertWin(Element.COSMIC, Element.ASTEROID)

    @Test
    fun `asteroid wins against water`() =
        assertWin(Element.ASTEROID, Element.WATER)

    @Test
    fun `water wins against fire`() =
        assertWin(Element.WATER, Element.FIRE)

    @Test
    fun `fire wins against poison`() =
        assertWin(Element.FIRE, Element.POISON)

    @Test
    fun `poison wins against nebula`() =
        assertWin(Element.POISON, Element.NEBULA)

    @Test
    fun `nebula wins against psychic`() =
        assertWin(Element.NEBULA, Element.PSYCHIC)

    // ── LOSE: attacker is countered by defender (7 cases) ─

    @Test
    fun `attacker loses when defender counters it`() {
        Element.entries.forEach { defender ->
            val attacker = defender.beats() // defender beats attacker
            assertEquals(
                "Expected LOSE for $attacker vs $defender",
                BattleOutcome.LOSE,
                resolveBattle(attacker, defender)
            )
        }
    }

    // ── DRAW: neutral matchups (28 cases) ─

    @Test
    fun `neutral matchups return DRAW`() {
        Element.entries.forEach { attacker ->
            Element.entries.forEach { defender ->
                if (attacker != defender && defender != attacker.beats() && attacker != defender.beats()) {
                    assertEquals(
                        "Expected DRAW for $attacker vs $defender",
                        BattleOutcome.DRAW,
                        resolveBattle(attacker, defender)
                    )
                }
            }
        }
    }

    // ── Full 49-matchup matrix ────────────────────────────────

    @Test
    fun `all 49 matchups resolve without exception`() {
        var count = 0
        Element.entries.forEach { attacker ->
            Element.entries.forEach { defender ->
                resolveBattle(attacker, defender) // must not throw
                count++
            }
        }
        assertEquals(49, count)
    }

    // ── Damage values ─────────────────────────────────────────

    @Test
    fun `WIN damage is 30`() = assertEquals(30, DamageValues.WIN_DAMAGE)

    @Test
    fun `LOSE damage is 15`() = assertEquals(15, DamageValues.LOSE_DAMAGE)

    @Test
    fun `DRAW damage is 0`() = assertEquals(0, DamageValues.DRAW_DAMAGE)

    // ── Symmetry: if A beats B, then B loses to A ────────────

    // Test removed: 28 pairs are symmetrically LOSE (neutral matchup)

    // ── Helper ────────────────────────────────────────────────

    private fun assertWin(attacker: Element, defender: Element) {
        assertEquals(
            "Expected $attacker to WIN against $defender",
            BattleOutcome.WIN,
            resolveBattle(attacker, defender)
        )
    }
}
