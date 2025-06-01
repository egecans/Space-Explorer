package com.example.spaceexplorer.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "launches")
data class LaunchEntity(
    @PrimaryKey val id: String,
    val missionName: String,
    val launchDateUtc: String,
    val rocketId: String,
    val rocketName: String,
    val success: Boolean?,
    val webcastUrl: String?,
    val articleUrl: String?,
    val wikipediaUrl: String?
)