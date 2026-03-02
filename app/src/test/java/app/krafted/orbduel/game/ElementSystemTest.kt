package app.krafted.orbduel.game

import org.junit.Assert.assertEquals
import org.junit.Test

class ElementSystemTest {

    // ── Weakness ring ────────────────────────────────────────

    @Test
    fun `weakness ring is complete and circular`() {
        assertEquals(Element.COSMIC,   Element.PSYCHIC.weakness())
        assertEquals(Element.ASTEROID, Element.COSMIC.weakness())
        assertEquals(Element.WATER,    Element.ASTEROID.weakness())
        assertEquals(Element.FIRE,     Element.WATER.weakness())
        assertEquals(Element.POISON,   Element.FIRE.weakness())
        assertEquals(Element.NEBULA,   Element.POISON.weakness())
        assertEquals(Element.PSYCHIC,  Element.NEBULA.weakness())
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

    // ── LOSE: attacker loses to defender (remaining 35 cases) ─

    @Test
    fun `all non-winning non-draw matchups return LOSE`() {
        Element.entries.forEach { attacker ->
            Element.entries.forEach { defender ->
                if (attacker != defender && defender != attacker.weakness()) {
                    assertEquals(
                        "Expected LOSE for $attacker vs $defender",
                        BattleOutcome.LOSE,
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

    @Test
    fun `outcome is antisymmetric for every non-draw pair`() {
        Element.entries.forEach { a ->
            Element.entries.forEach { b ->
                if (a != b) {
                    val ab = resolveBattle(a, b)
                    val ba = resolveBattle(b, a)
                    val isOpposite = (ab == BattleOutcome.WIN && ba == BattleOutcome.LOSE)
                            || (ab == BattleOutcome.LOSE && ba == BattleOutcome.WIN)
                    assert(isOpposite) {
                        "$a vs $b → $ab, but $b vs $a → $ba (expected opposite)"
                    }
                }
            }
        }
    }

    // ── Helper ────────────────────────────────────────────────

    private fun assertWin(attacker: Element, defender: Element) {
        assertEquals(
            "Expected $attacker to WIN against $defender",
            BattleOutcome.WIN,
            resolveBattle(attacker, defender)
        )
    }
}
