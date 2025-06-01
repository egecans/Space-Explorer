package com.example.spaceexplorer.domain.usecase

import com.example.spaceexplorer.domain.model.Rocket
import com.example.spaceexplorer.domain.repository.LaunchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class GetRocketByIdUseCaseTest {

    private lateinit var repository: com.example.spaceexplorer.domain.repository.LaunchRepository
    private lateinit var useCase: com.example.spaceexplorer.domain.usecase.GetRocketByIdUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = com.example.spaceexplorer.domain.usecase.GetRocketByIdUseCase(repository)
    }

    @Test
    fun `invoke returns rocket from repository`() = runTest {
        val rocket =
            com.example.spaceexplorer.domain.model.Rocket("rocket1", "Falcon 9", "Description")
        whenever(repository.getRocketById("rocket1")).thenReturn(rocket)

        val result = useCase("rocket1")

        assertEquals(rocket, result)
        verify(repository).getRocketById("rocket1")
    }
}
