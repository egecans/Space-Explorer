package com.example.spaceexplorer.presentation.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.spaceexplorer.domain.model.Launch
import com.example.spaceexplorer.domain.model.Rocket
import com.example.spaceexplorer.domain.usecase.GetLaunchByIdUseCase
import com.example.spaceexplorer.domain.usecase.GetRocketByIdUseCase
import com.example.spaceexplorer.presentation.model.LaunchDetailUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.rules.TestRule
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    // Ensures LiveData / coroutines run synchronously in tests
    @get:Rule
    val instantExecutorRule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getLaunchByIdUseCase: com.example.spaceexplorer.domain.usecase.GetLaunchByIdUseCase
    private lateinit var getRocketByIdUseCase: com.example.spaceexplorer.domain.usecase.GetRocketByIdUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: DetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getLaunchByIdUseCase = mock()
        getRocketByIdUseCase = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init with null launchId sets Error state`() = runTest {
        savedStateHandle = SavedStateHandle() // no launchId
        viewModel = DetailViewModel(getLaunchByIdUseCase, getRocketByIdUseCase, savedStateHandle)

        val state = viewModel.uiState.first()
        Assert.assertTrue(state is LaunchDetailUiState.Error)
        Assert.assertEquals("No launch ID provided", (state as LaunchDetailUiState.Error).message)
    }

    @Test
    fun `fetchLaunchDetail sets Success state when launch and rocket found`() = runTest {
        val launch = com.example.spaceexplorer.domain.model.Launch(
            id = "launch1",
            missionName = "Test Mission",
            launchDateUtc = "2024-01-01T00:00:00Z",
            rocketId = "rocket1",
            rocketName = "Test Rocket",
            success = true,
            webcastUrl = null,
            articleUrl = null,
            wikipediaUrl = null
        )
        val rocket = com.example.spaceexplorer.domain.model.Rocket(
            id = "rocket1",
            name = "Test Rocket",
            description = "Test Description"
        )

        whenever(getLaunchByIdUseCase.invoke("launch1")).thenReturn(launch)
        whenever(getRocketByIdUseCase.invoke("rocket1")).thenReturn(rocket)

        savedStateHandle = SavedStateHandle(mapOf("launchId" to "launch1"))
        viewModel = DetailViewModel(getLaunchByIdUseCase, getRocketByIdUseCase, savedStateHandle)

        // Advance coroutines to process fetch
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        Assert.assertTrue(state is LaunchDetailUiState.Success)
        val successState = state as LaunchDetailUiState.Success
        Assert.assertEquals(launch, successState.launch)
        Assert.assertEquals(rocket, successState.rocket)
    }

    @Test
    fun `fetchLaunchDetail sets Error state when launch not found`() = runTest {
        whenever(getLaunchByIdUseCase.invoke("launch1")).thenReturn(null)

        savedStateHandle = SavedStateHandle(mapOf("launchId" to "launch1"))
        viewModel = DetailViewModel(getLaunchByIdUseCase, getRocketByIdUseCase, savedStateHandle)

        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        Assert.assertTrue(state is LaunchDetailUiState.Error)
        Assert.assertEquals("Launch not found", (state as LaunchDetailUiState.Error).message)
    }

    @Test
    fun `fetchLaunchDetail sets Error state when rocket not found`() = runTest {
        val launch = com.example.spaceexplorer.domain.model.Launch(
            id = "launch1",
            missionName = "Test Mission",
            launchDateUtc = "2024-01-01T00:00:00Z",
            rocketId = "rocket1",
            rocketName = "Test Rocket",
            success = true,
            webcastUrl = null,
            articleUrl = null,
            wikipediaUrl = null
        )
        whenever(getLaunchByIdUseCase.invoke("launch1")).thenReturn(launch)
        whenever(getRocketByIdUseCase.invoke("rocket1")).thenReturn(null)

        savedStateHandle = SavedStateHandle(mapOf("launchId" to "launch1"))
        viewModel = DetailViewModel(getLaunchByIdUseCase, getRocketByIdUseCase, savedStateHandle)

        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        Assert.assertTrue(state is LaunchDetailUiState.Error)
        Assert.assertEquals("Rocket not found", (state as LaunchDetailUiState.Error).message)
    }

    @Test
    fun `fetchLaunchDetail sets Error state on exception`() = runTest {
        whenever(getLaunchByIdUseCase.invoke("launch1")).thenThrow(RuntimeException("Test exception"))

        savedStateHandle = SavedStateHandle(mapOf("launchId" to "launch1"))
        viewModel = DetailViewModel(getLaunchByIdUseCase, getRocketByIdUseCase, savedStateHandle)

        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        Assert.assertTrue(state is LaunchDetailUiState.Error)
        Assert.assertEquals("Test exception", (state as LaunchDetailUiState.Error).message)
    }
}
