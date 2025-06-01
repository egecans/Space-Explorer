package com.example.spaceexplorer.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LaunchDao {

    @Query("SELECT * FROM launches")
    fun getLaunches(): Flow<List<LaunchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaunches(launches: List<LaunchEntity>)

    @Query("SELECT * FROM launches WHERE id = :id LIMIT 1")
    suspend fun getLaunchById(id: String): LaunchEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRockets(rockets: List<RocketEntity>)

    @Query("SELECT * FROM rockets WHERE id = :id LIMIT 1")
    suspend fun getRocketById(id: String): RocketEntity?
}
