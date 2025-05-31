package com.example.spaceexplorer.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.example.spaceexplorer.domain.model.Launch
import com.example.spaceexplorer.domain.usecase.GetLaunchByIdUseCase
import com.example.spaceexplorer.presentation.model.LaunchDetailUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getLaunchByIdUseCase: GetLaunchByIdUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: DetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getLaunchByIdUseCase = mock()
        savedStateHandle = SavedStateHandle(mapOf("launchId" to "id1"))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchLaunchDetail emits Loading and Success states`() = runTest {
        val launch = Launch("id1", "Mission 1", "2024-01-01T00:00:00Z", "r1", "Rocket 1", true, null, null, null)
        whenever(getLaunchByIdUseCase.invoke("id1")).thenReturn(launch)

        viewModel = DetailViewModel(getLaunchByIdUseCase, savedStateHandle)

        val states = mutableListOf<LaunchDetailUiState>()
        val job = launch {
            viewModel.uiState.toList(states)
        }

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(LaunchDetailUiState.Loading, states[0])
        assertEquals(LaunchDetailUiState.Success(launch), states[1])

        job.cancel()
    }

    @Test
    fun `fetchLaunchDetail emits Error when launch not found`() = runTest {
        whenever(getLaunchByIdUseCase.invoke("id1")).thenReturn(null)

        viewModel = DetailViewModel(getLaunchByIdUseCase, savedStateHandle)

        val states = mutableListOf<LaunchDetailUiState>()
        val job = launch {
            viewModel.uiState.toList(states)
        }

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(LaunchDetailUiState.Loading, states[0])
        assertTrue(states[1] is LaunchDetailUiState.Error)

        job.cancel()
    }
}
