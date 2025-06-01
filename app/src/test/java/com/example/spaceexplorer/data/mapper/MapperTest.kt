package com.example.spaceexplorer.data.mapper

import com.example.spaceexplorer.data.mapper.toDomain
import com.example.spaceexplorer.data.model.LaunchDto
import com.example.spaceexplorer.data.model.LinksDto
import com.example.spaceexplorer.data.model.RocketDto
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class MapperTest {

    @Test
    fun `rocketDto toDomain maps correctly`() {
        val rocketDto = com.example.spaceexplorer.data.model.RocketDto(
            id = "rocket123",
            name = "Falcon 1",
            description = "Test description"
        )

        val rocket = rocketDto.toDomain()

        assertEquals("rocket123", rocket.id)
        assertEquals("Falcon 1", rocket.name)
        assertEquals("Test description", rocket.description)
    }

    @Test
    fun `launchDto toDomain maps correctly with rocketName`() {
        val launchDto = com.example.spaceexplorer.data.model.LaunchDto(
            id = "launch123",
            name = "Mission 1",
            dateUtc = "2023-01-01T00:00:00Z",
            rocket = "rocket123",
            success = true,
            links = com.example.spaceexplorer.data.model.LinksDto(
                webcast = "https://youtube.com/test",
                article = "https://article.com/test",
                wikipedia = "https://wikipedia.org/test"
            )
        )

        val launch = launchDto.toDomain(rocketName = "Falcon 1")

        assertEquals("launch123", launch.id)
        assertEquals("Mission 1", launch.missionName)
        assertEquals("2023-01-01T00:00:00Z", launch.launchDateUtc)
        assertEquals("rocket123", launch.rocketId)
        assertEquals("Falcon 1", launch.rocketName)
        assertTrue(launch.success ?: true)
        assertEquals("https://youtube.com/test", launch.webcastUrl)
        assertEquals("https://article.com/test", launch.articleUrl)
        assertEquals("https://wikipedia.org/test", launch.wikipediaUrl)
    }
}
