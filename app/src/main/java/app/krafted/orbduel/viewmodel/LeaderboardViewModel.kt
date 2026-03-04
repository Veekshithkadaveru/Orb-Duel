package app.krafted.orbduel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.krafted.orbduel.data.MatchDao
import app.krafted.orbduel.data.LeaderboardEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    matchDao: MatchDao
) : ViewModel() {

    val topRecords: StateFlow<List<LeaderboardEntry>> = matchDao.getLeaderboard()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
