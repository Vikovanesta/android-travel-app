package com.example.uts.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.uts.model.Station
import com.example.uts.model.Travel

@Database(
    entities = [
        Travel::class,
        Station::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TravelDatabase: RoomDatabase() {
abstract fun travelDao(): TravelDao?
abstract fun stationDao(): StationDao?

    companion object {
        @Volatile
        private var INSTANCE: TravelDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: android.content.Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: getDatabase(context).also { INSTANCE = it }
        }

        private fun getDatabase(context: android.content.Context): TravelDatabase? {
            if (INSTANCE == null) {
                synchronized(TravelDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = androidx.room.Room.databaseBuilder(
                            context.applicationContext,
                            TravelDatabase::class.java, "travel_db.db"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}