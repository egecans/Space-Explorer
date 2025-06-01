package com.example.spaceexplorer.presentation.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.spaceexplorer.common.error.NoInternetException
import com.example.spaceexplorer.domain.model.Launch
import com.example.spaceexplorer.domain.usecase.GetLaunchesUseCase
import com.example.spaceexplorer.presentation.model.LaunchesUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.rules.TestRule
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getLaunchesUseCase: GetLaunchesUseCase
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getLaunchesUseCase = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchLaunches emits Success state`() = runTest {
        val launches = listOf(
            Launch("id1", "Mission 1", "2024-01-01T00:00:00Z", "r1", "Rocket 1", true, null, null, null)
        )
        whenever(getLaunchesUseCase()).thenReturn(flow { emit(launches) })

        viewModel = HomeViewModel(getLaunchesUseCase)

        val states = mutableListOf<LaunchesUiState>()
        val job = launch {
            viewModel.uiState.toList(states)
        }

        testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertTrue(states.any { it is LaunchesUiState.Success && it.launches == launches })

        job.cancel()
    }

    @Test
    fun `fetchLaunches emits NoInternetConnection state`() = runTest {
        whenever(getLaunchesUseCase()).thenReturn(flow { throw NoInternetException("") })

        viewModel = HomeViewModel(getLaunchesUseCase)

        val states = mutableListOf<LaunchesUiState>()
        val job = launch {
            viewModel.uiState.toList(states)
        }

        testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertTrue(states.any { it is LaunchesUiState.NoInternetConnection})

        job.cancel()
    }

    @Test
    fun `fetchLaunches emits Error state on generic exception`() = runTest {
        val errorMessage = "Unexpected error"
        whenever(getLaunchesUseCase()).thenReturn(flow { throw RuntimeException(errorMessage) })

        viewModel = HomeViewModel(getLaunchesUseCase)

        val states = mutableListOf<LaunchesUiState>()
        val job = launch {
            viewModel.uiState.toList(states)
        }

        testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertTrue(states.any { it is LaunchesUiState.Error && it.message == errorMessage })

        job.cancel()
    }
}
