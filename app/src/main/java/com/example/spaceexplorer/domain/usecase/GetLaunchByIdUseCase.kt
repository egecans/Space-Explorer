package com.example.spaceexplorer.domain.usecase

import com.example.spaceexplorer.domain.model.Launch
import com.example.spaceexplorer.domain.repository.LaunchRepository
import javax.inject.Inject

/**
 * Use case to get detailed information of a single launch by its ID.
 *
 * Abstracts away data source details from UI layers.
 *
 * @property repository Repository instance injected via DI.
 */
class GetLaunchByIdUseCase @Inject constructor(
    private val repository: LaunchRepository
) {

    /**
     * Retrieves a [Launch] matching the given [id].
     *
     * @param id The unique identifier of the launch.
     * @return Launch data or null if not found.
     */
    suspend operator fun invoke(id: String): Launch? = repository.getLaunchById(id)
}
