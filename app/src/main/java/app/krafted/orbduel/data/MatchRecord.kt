package app.krafted.orbduel.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match_records")
data class MatchRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val winnerName: String,
    val gameMode: String,      // "VS_AI" or "VS_PLAYER"
    val difficulty: String,    // "EASY" / "MEDIUM" / "HARD" / "N/A"
    val roundsPlayed: Int,
    val timestamp: Long = System.currentTimeMillis()
)

