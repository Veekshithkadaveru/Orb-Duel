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
}

