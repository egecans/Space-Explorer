package com.example.spaceexplorer.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for Launches.
 * Represents a launch with its details.
 * Only include fields we actually need for the UI, to keep models minimal.
 * Success is nullable because some launches may lack that info
 */
data class LaunchDto(
    val id: String,
    val name: String,
    @SerializedName("date_utc") val dateUtc: String,
    val rocket: String,          // Rocket ID
    val success: Boolean?,
    val links: LinksDto,
)

/**
 * Data Transfer Object for Links associated with a Launch.
 */
data class LinksDto(
    val webcast: String?,
    val article: String?,
    val wikipedia: String?
)



