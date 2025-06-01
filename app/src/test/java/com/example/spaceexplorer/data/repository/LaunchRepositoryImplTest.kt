package com.example.spaceexplorer.data.repository

import android.content.Context
import com.example.spaceexplorer.data.api.SpaceXApiService
import com.example.spaceexplorer.data.model.LaunchDto
import com.example.spaceexplorer.data.model.LinksDto
import com.example.spaceexplorer.data.model.RocketDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class LaunchRepositoryImplTest {

    @Mock
    private lateinit var apiService: com.example.spaceexplorer.data.api.SpaceXApiService

    @Mock
    private lateinit var context: Context

    private lateinit var repository: com.example.spaceexplorer.data.repository.LaunchRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository =
            com.example.spaceexplorer.data.repository.LaunchRepositoryImpl(apiService, context)
    }

    // Need to add network util to di, to not give error
    @Test
    fun `getLaunches returns mapped launches`() = runTest {
        val rocketDto = com.example.spaceexplorer.data.model.RocketDto(
            "rocket1",
            "Falcon 9",
            "Description of Falcon 9"
        )
        val launchDto = com.example.spaceexplorer.data.model.LaunchDto(
            id = "launch1",
            name = "Starlink Mission",
            dateUtc = "2024-01-01T00:00:00Z",
            rocket = "rocket1",
            success = true,
            links = com.example.spaceexplorer.data.model.LinksDto(
                webcast = "https://youtube.com/test",
                article = "https://article.com/test",
                wikipedia = "https://wikipedia.org/test"
            )
        )

        whenever(apiService.getLaunches()).thenReturn(listOf(launchDto))
        whenever(apiService.getRockets()).thenReturn(listOf(rocketDto))

        val launches = repository.getLaunches().first()

        assertEquals(1, launches.size)
        val launch = launches[0]
        assertEquals("launch1", launch.id)
        assertEquals("Starlink Mission", launch.missionName)
        assertEquals("Falcon 9", launch.rocketName)
        assertTrue(launch.success == true)
    }

    @Test
    fun `getLaunchById returns launch with rocket name`() = runTest {
        val rocketDto = com.example.spaceexplorer.data.model.RocketDto(
            "rocket1",
            "Falcon 9",
            "Description of Falcon 9"
        )
        val launchDto = com.example.spaceexplorer.data.model.LaunchDto(
            id = "launch1",
            name = "Starlink Mission",
            dateUtc = "2024-01-01T00:00:00Z",
            rocket = "rocket1",
            success = true,
            links = com.example.spaceexplorer.data.model.LinksDto(
                webcast = "https://youtube.com/test",
                article = "https://article.com/test",
                wikipedia = "https://wikipedia.org/test"
            )
        )

        whenever(apiService.getLaunchById("launch1")).thenReturn(launchDto)
        whenever(apiService.getRocketById("rocket1")).thenReturn(rocketDto)

        val launch = repository.getLaunchById("launch1")

        assertTrue(launch != null)
        assertEquals("Falcon 9", launch?.rocketName)
    }

    @Test
    fun `getRocketById returns mapped rocket`() = runTest {
        val rocketDto = com.example.spaceexplorer.data.model.RocketDto(
            "rocket1",
            "Falcon 9",
            "Description of Falcon 9"
        )

        whenever(apiService.getRocketById("rocket1")).thenReturn(rocketDto)

        val rocket = repository.getRocketById("rocket1")

        assertTrue(rocket != null)
        assertEquals("Falcon 9", rocket?.name)
        assertEquals("Description of Falcon 9", rocket?.description)
    }

}
