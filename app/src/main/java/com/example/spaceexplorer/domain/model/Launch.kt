package com.example.spaceexplorer.domain.model

/**
 * Data class representing a SpaceX launch.
 */
data class Launch(
    val id: String,
    val missionName: String,
    val launchDateUtc: String,
    val rocketId: String,
    val rocketName: String,
    val success: Boolean?,
    val webcastUrl: String?,
    val articleUrl: String?,
    val wikipediaUrl: String?,
)


