package app.krafted.orbduel.data

data class LeaderboardEntry(
    val playerName: String,
    val wins: Int,
    val fastestRound: Int,
    val highestHp: Int
)
