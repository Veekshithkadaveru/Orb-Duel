package app.krafted.orbduel.game

enum class TurnPhase {
    PLAYER1_SELECT,
    PLAYER2_SELECT,
    REVEAL,
    RESULT
}

enum class GameMode   { VS_AI, VS_PLAYER }
enum class Difficulty { EASY, MEDIUM, HARD }
enum class Player     { PLAYER1, PLAYER2 }

data class RoundResult(
    val player1Orb:      Element,
    val player2Orb:      Element,
    val player1Outcome:  BattleOutcome,
    val damageTakenByP1: Int,
    val damageTakenByP2: Int
)

data class BattleUiState(
    val player1Hp:          Int        = 150,
    val player2Hp:          Int        = 150,
    val player1SelectedOrb: Element?   = null,
    val player2SelectedOrb: Element?   = null,
    val currentTurn:        TurnPhase  = TurnPhase.PLAYER1_SELECT,
    val lastRoundResult:    RoundResult? = null,
    val matchWinner:        Player?    = null,
    val roundCount:         Int        = 0,
    val gameMode:           GameMode   = GameMode.VS_AI,
    val aiDifficulty:       Difficulty = Difficulty.MEDIUM,
    val player2Name:        String     = "AI"
)

const val MAX_ROUNDS = 10

fun resolveRound(
    p1Orb:        Element,
    p2Orb:        Element,
    currentState: BattleUiState
): BattleUiState {
    val p1Outcome = resolveBattle(p1Orb, p2Orb)

    val damageTakenByP1: Int
    val damageTakenByP2: Int

    when (p1Outcome) {
        BattleOutcome.WIN  -> { damageTakenByP1 = 0;                      damageTakenByP2 = DamageValues.WIN_DAMAGE  }
        BattleOutcome.LOSE -> { damageTakenByP1 = DamageValues.WIN_DAMAGE; damageTakenByP2 = 0                       }
        BattleOutcome.DRAW -> { damageTakenByP1 = DamageValues.DRAW_DAMAGE; damageTakenByP2 = DamageValues.DRAW_DAMAGE }
    }

    val newP1Hp = (currentState.player1Hp - damageTakenByP1).coerceAtLeast(0)
    val newP2Hp = (currentState.player2Hp - damageTakenByP2).coerceAtLeast(0)

    val newRoundCount = currentState.roundCount + 1
    val maxRoundsReached = newRoundCount >= MAX_ROUNDS

    val winner: Player? = when {
        newP1Hp <= 0 && newP2Hp <= 0 -> null
        newP1Hp <= 0                 -> Player.PLAYER2
        newP2Hp <= 0                 -> Player.PLAYER1
        maxRoundsReached && newP1Hp > newP2Hp  -> Player.PLAYER1
        maxRoundsReached && newP2Hp > newP1Hp  -> Player.PLAYER2
        maxRoundsReached                       -> null
        else                         -> null
    }

    val gameOver = winner != null || maxRoundsReached

    return currentState.copy(
        player1Hp          = newP1Hp,
        player2Hp          = newP2Hp,
        player1SelectedOrb = null,
        player2SelectedOrb = null,
        lastRoundResult    = RoundResult(p1Orb, p2Orb, p1Outcome, damageTakenByP1, damageTakenByP2),
        matchWinner        = winner,
        roundCount         = newRoundCount,
        currentTurn        = if (gameOver) TurnPhase.RESULT else TurnPhase.REVEAL
    )
}
