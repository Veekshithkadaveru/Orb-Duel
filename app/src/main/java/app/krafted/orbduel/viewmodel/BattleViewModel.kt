package app.krafted.orbduel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.krafted.orbduel.data.MatchDao
import app.krafted.orbduel.data.MatchRecord
import app.krafted.orbduel.game.AiOpponent
import app.krafted.orbduel.game.BattleUiState
import app.krafted.orbduel.game.Difficulty
import app.krafted.orbduel.game.Element
import app.krafted.orbduel.game.GameMode
import app.krafted.orbduel.game.Player
import app.krafted.orbduel.game.TurnPhase
import app.krafted.orbduel.game.resolveRound
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class BattleViewModel @Inject constructor(
    private val aiOpponent: AiOpponent,
    private val matchDao: MatchDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(BattleUiState())
    val uiState: StateFlow<BattleUiState> = _uiState

    private var hasSavedResultForCurrentMatch: Boolean = false

    fun setGameMode(
        mode: GameMode,
        player2Name: String = if (mode == GameMode.VS_AI) "AI" else "Player 2"
    ) {
        _uiState.value = BattleUiState(
            gameMode = mode,
            aiDifficulty = _uiState.value.aiDifficulty,
            player2Name = player2Name
        )
        hasSavedResultForCurrentMatch = false
        aiOpponent.reset()
    }

    fun setAiDifficulty(difficulty: Difficulty) {
        _uiState.value = _uiState.value.copy(aiDifficulty = difficulty)
    }

    fun selectPlayer1Orb(orb: Element) {
        if (_uiState.value.currentTurn != TurnPhase.PLAYER1_SELECT) return
        _uiState.value = _uiState.value.copy(player1SelectedOrb = orb)
    }

    fun confirmPlayer1Selection() {
        val state = _uiState.value
        if (state.currentTurn != TurnPhase.PLAYER1_SELECT || state.player1SelectedOrb == null) {
            return
        }

        when (state.gameMode) {
            GameMode.VS_AI -> handleAiTurnAfterPlayerConfirm(state)
            GameMode.VS_PLAYER -> {
                _uiState.value = state.copy(currentTurn = TurnPhase.PLAYER2_SELECT)
            }
        }
    }

    fun selectPlayer2Orb(orb: Element) {
        if (_uiState.value.currentTurn != TurnPhase.PLAYER2_SELECT) return
        _uiState.value = _uiState.value.copy(player2SelectedOrb = orb)
    }

    fun confirmPlayer2Selection() {
        val state = _uiState.value
        if (state.currentTurn != TurnPhase.PLAYER2_SELECT ||
            state.player1SelectedOrb == null ||
            state.player2SelectedOrb == null
        ) {
            return
        }

        val newState = resolveRound(
            p1Orb = state.player1SelectedOrb,
            p2Orb = state.player2SelectedOrb,
            currentState = state
        )
        _uiState.value = newState
        saveMatchResultIfNeeded(newState)
    }

    fun nextRound() {
        val state = _uiState.value
        if (state.matchWinner != null) return

        _uiState.value = state.copy(
            currentTurn = TurnPhase.PLAYER1_SELECT
        )
    }

    fun resetMatch() {
        val state = _uiState.value
        _uiState.value = BattleUiState(
            gameMode = state.gameMode,
            aiDifficulty = state.aiDifficulty,
            player2Name = state.player2Name
        )
        hasSavedResultForCurrentMatch = false
        aiOpponent.reset()
    }

    private fun handleAiTurnAfterPlayerConfirm(state: BattleUiState) {
        val playerOrb = state.player1SelectedOrb ?: return

        aiOpponent.recordPlayerOrb(playerOrb)
        val aiOrb = aiOpponent.pickOrb(state.aiDifficulty, playerOrb)

        val newState = resolveRound(
            p1Orb = playerOrb,
            p2Orb = aiOrb,
            currentState = state.copy(player2SelectedOrb = aiOrb)
        )
        _uiState.value = newState
        saveMatchResultIfNeeded(newState)
    }

    private fun saveMatchResultIfNeeded(state: BattleUiState) {
        val winner = state.matchWinner ?: return
        if (hasSavedResultForCurrentMatch) return

        hasSavedResultForCurrentMatch = true

        val winnerName = when (winner) {
            Player.PLAYER1 -> "Player 1"
            Player.PLAYER2 -> state.player2Name
        }

        val gameModeString = state.gameMode.name
        val difficultyString = when (state.gameMode) {
            GameMode.VS_AI -> state.aiDifficulty.name
            GameMode.VS_PLAYER -> "N/A"
        }

        val record = MatchRecord(
            winnerName = winnerName,
            gameMode = gameModeString,
            difficulty = difficultyString,
            roundsPlayed = state.roundCount
        )

        viewModelScope.launch {
            matchDao.insertMatch(record)
        }
    }
}

