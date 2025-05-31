package com.example.spaceexplorer.domain.usecase

import com.example.spaceexplorer.domain.model.Launch
import com.example.spaceexplorer.domain.repository.LaunchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GetLaunchByIdUseCaseTest {

    private lateinit var repository: LaunchRepository
    private lateinit var useCase: GetLaunchByIdUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = GetLaunchByIdUseCase(repository)
    }

    @Test
    fun `invoke returns launch when found`() = runTest {
        val launch = Launch("id1", "Mission 1", "2024-01-01T00:00:00Z", "r1", "Rocket 1", true, null, null, null)
        whenever(repository.getLaunchById("id1")).thenReturn(launch)

        val result = useCase("id1")

        assertEquals(launch, result)
        verify(repository).getLaunchById("id1")
    }

    @Test
    fun `invoke returns null when not found`() = runTest {
        whenever(repository.getLaunchById("unknown")).thenReturn(null)

        val result = useCase("unknown")

        assertNull(result)
        verify(repository).getLaunchById("unknown")
    }
}
