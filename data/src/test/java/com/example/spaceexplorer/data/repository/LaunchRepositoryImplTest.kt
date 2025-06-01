package com.example.spaceexplorer.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.spaceexplorer.common.NetworkChecker
import com.example.spaceexplorer.common.NetworkUtils
import com.example.spaceexplorer.data.api.SpaceXApiService
import com.example.spaceexplorer.data.db.LaunchDao
import com.example.spaceexplorer.data.db.SpaceExplorerDatabase
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
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.mockStatic
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class) // it prevents giving error about logs
class LaunchRepositoryImplTest {

    @Mock
    private lateinit var apiService: SpaceXApiService

    @Mock
    private lateinit var database: SpaceExplorerDatabase

    @Mock
    private lateinit var launchDao: LaunchDao

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var sharedPrefsEditor: SharedPreferences.Editor

    @Mock
    private lateinit var networkChecker: NetworkChecker

    private lateinit var repository: LaunchRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)



        whenever(database.launchDao()).thenReturn(launchDao) // Mock Dao from database
        // Mock network checker to simulate online or offline
        whenever(networkChecker.isNetworkAvailable(any())).thenReturn(true) // or false for offline

        repository = LaunchRepositoryImpl(apiService, database, context, networkChecker)

    }

    // Need to add network util to di, to not give error
    @Test
    fun `getLaunches returns mapped launches`() = runTest {

        // for the sharedPref error
        whenever(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences)
        whenever(sharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(false)
        whenever(sharedPreferences.edit()).thenReturn(sharedPrefsEditor)
        whenever(sharedPrefsEditor.putBoolean(anyString(), anyBoolean())).thenReturn(sharedPrefsEditor)
        whenever(sharedPrefsEditor.apply()).thenAnswer { /* do nothing */ }


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
