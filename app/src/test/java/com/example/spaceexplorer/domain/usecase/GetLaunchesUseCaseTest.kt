package com.example.spaceexplorer.domain.usecase

import com.example.spaceexplorer.domain.model.Launch
import com.example.spaceexplorer.domain.repository.LaunchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GetLaunchesUseCaseTest {

    private lateinit var repository: com.example.spaceexplorer.domain.repository.LaunchRepository
    private lateinit var useCase: com.example.spaceexplorer.domain.usecase.GetLaunchesUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = com.example.spaceexplorer.domain.usecase.GetLaunchesUseCase(repository)
    }

    @Test
    fun `invoke returns launches flow from repository`() = runTest {
        val expectedLaunches = listOf(
            com.example.spaceexplorer.domain.model.Launch(
                "id1",
                "Mission 1",
                "2024-01-01T00:00:00Z",
                "r1",
                "Rocket 1",
                true,
                null,
                null,
                null
            )
        )
        whenever(repository.getLaunches()).thenReturn(flowOf(expectedLaunches))

        val emissions = useCase().toList()

        assertEquals(1, emissions.size)
        assertEquals(expectedLaunches, emissions[0])
        verify(repository).getLaunches()
    }
}
