package com.example.spaceexplorer.data.mapper

import com.example.spaceexplorer.data.model.LaunchDto
import com.example.spaceexplorer.data.model.RocketDto
import com.example.spaceexplorer.domain.model.Launch
import com.example.spaceexplorer.domain.model.Rocket

// Map RocketDto to Rocket domain model
fun RocketDto.toDomain(): Rocket = Rocket(
    id = id,
    name = name,
    description = description,
)

// Map LaunchDto + rocketName to domain Launch model
fun LaunchDto.toDomain(rocketName: String): Launch = Launch(
    id = id,
    missionName = name,
    launchDateUtc = dateUtc,
    rocketId = rocket,
    rocketName = rocketName,
    success = success,
    webcastUrl = links.webcast,
    articleUrl = links.article,
    wikipediaUrl = links.wikipedia,
)
