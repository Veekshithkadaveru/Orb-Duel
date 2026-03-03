package app.krafted.orbduel.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MatchRecord::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun matchDao(): MatchDao

    companion object {
        const val DATABASE_NAME: String = "orbduel.db"
    }
}

