package com.example.spaceexplorer.data.mapper

import com.example.spaceexplorer.data.db.LaunchEntity
import com.example.spaceexplorer.data.db.RocketEntity
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

// Map LaunchEntity to Launch domain model without rocketName
fun LaunchEntity.toDomain(rocketName: String): Launch = Launch(
    id = id,
    missionName = missionName,
    launchDateUtc = launchDateUtc,
    rocketId = rocketId,
    rocketName = rocketName,
    success = success,
    webcastUrl = webcastUrl,
    articleUrl = articleUrl,
    wikipediaUrl = wikipediaUrl
)

fun Launch.toEntity(): LaunchEntity = LaunchEntity(
    id = id,
    missionName = missionName,
    launchDateUtc = launchDateUtc,
    rocketId = rocketId,
    rocketName = rocketName,
    success = success,
    webcastUrl = webcastUrl,
    articleUrl = articleUrl,
    wikipediaUrl = wikipediaUrl
)

fun Rocket.toEntity(): RocketEntity = RocketEntity(
    id = id,
    name = name,
    description = description
)

fun RocketDto.toEntity(): RocketEntity = RocketEntity(
    id = id,
    name = name,
    description = description
)

// rocket entity to domain
fun RocketEntity.toDomain(): Rocket = Rocket(
    id = id,
    name = name,
    description = description
)