package com.example.spaceexplorer.domain.usecase

import com.example.spaceexplorer.domain.model.Launch
import com.example.spaceexplorer.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to retrieve a flow of SpaceX launches.
 *
 * Encapsulates domain logic related to getting launches.
 * This class is part of the domain layer and acts as an
 * abstraction over the repository.
 *
 * @property repository An instance of [LaunchRepository] provided via dependency injection.
 */
class GetLaunchesUseCase @Inject constructor(
    private val repository: LaunchRepository
) {

    /**
     * Returns a flow emitting a list of [Launch].
     *
     * Consumers can collect this flow to get updates of launches data.
     */
    operator fun invoke(): Flow<List<Launch>> = repository.getLaunches()
}
