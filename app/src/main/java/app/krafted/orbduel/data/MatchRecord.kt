package app.krafted.orbduel.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match_records")
data class MatchRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val winnerName: String,
    val gameMode: String,
    val difficulty: String,
    val roundsPlayed: Int,
    val remainingHp: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

