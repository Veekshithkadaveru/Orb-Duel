package app.krafted.orbduel.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {

    @Insert
    suspend fun insertMatch(record: MatchRecord)

    @Query("SELECT * FROM match_records ORDER BY timestamp DESC LIMIT 10")
    fun getTopRecords(): Flow<List<MatchRecord>>

    @Query(
        """
        SELECT winnerName as playerName, 
               COUNT(id) as wins, 
               MIN(roundsPlayed) as fastestRound, 
               MAX(remainingHp) as highestHp 
        FROM match_records 
        WHERE winnerName != 'STALEMATE' AND winnerName != 'AI' 
        GROUP BY winnerName 
        ORDER BY wins DESC, fastestRound ASC, highestHp DESC 
        LIMIT 10
    """
    )
    fun getLeaderboard(): Flow<List<LeaderboardEntry>>
}

