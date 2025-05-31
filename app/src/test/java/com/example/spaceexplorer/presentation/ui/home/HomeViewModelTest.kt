package com.example.spaceexplorer.presentation.ui.home

import com.example.spaceexplorer.domain.model.Launch
import com.example.spaceexplorer.domain.usecase.GetLaunchesUseCase
import com.example.spaceexplorer.presentation.model.LaunchesUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

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
    fun `fetchLaunches emits Loading and Success states`() = runTest {
        val launches = listOf(
            Launch("id1", "Mission 1", "2024-01-01T00:00:00Z", "r1", "Rocket 1", true, null, null, null)
        )
        whenever(getLaunchesUseCase()).thenReturn(flow {
            emit(launches)
        })

        viewModel = HomeViewModel(getLaunchesUseCase)

        val states = mutableListOf<LaunchesUiState>()

        val job = launch {
            viewModel.uiState.toList(states)
        }

        // Process all pending tasks
        advanceUntilIdle()

        assertEquals(LaunchesUiState.Loading, states[0])
        assertEquals(LaunchesUiState.Success(launches), states[1])

        job.cancel()
    }

}
