package com.example.spaceexplorer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LaunchEntity::class, RocketEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SpaceExplorerDatabase : RoomDatabase() {
    abstract fun launchDao(): LaunchDao
}
