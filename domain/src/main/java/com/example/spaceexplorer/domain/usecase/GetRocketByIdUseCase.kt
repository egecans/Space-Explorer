package com.example.spaceexplorer.domain.usecase

import com.example.spaceexplorer.domain.model.Rocket
import com.example.spaceexplorer.domain.repository.LaunchRepository
import javax.inject.Inject

/**
 * Use case to fetch rocket details by rocket ID.
 */
class GetRocketByIdUseCase @Inject constructor(
    private val repository: LaunchRepository
) {
    suspend operator fun invoke(id: String): Rocket? = repository.getRocketById(id)
}
