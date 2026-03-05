package app.krafted.orbduel.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {

    @Insert
    suspend fun insertMatch(record: MatchRecord)

    @Query(
        """
        SELECT winnerName as playerName,
               COUNT(id) as wins,
               SUM(CASE WHEN roundsPlayed < 10 THEN 1 ELSE 0 END) as hpKnockouts,
               MIN(roundsPlayed) as fastestRound,
               MAX(remainingHp) as highestHp
        FROM match_records
        WHERE winnerName != 'STALEMATE' AND NOT (gameMode = 'VS_AI' AND winnerName = 'AI')
        GROUP BY winnerName
        ORDER BY hpKnockouts DESC, wins DESC, fastestRound ASC, highestHp DESC
        LIMIT 10
    """
    )
    fun getLeaderboard(): Flow<List<LeaderboardEntry>>
}

