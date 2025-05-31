package com.example.spaceexplorer.domain.repository

import com.example.spaceexplorer.domain.model.Launch
import com.example.spaceexplorer.domain.model.Rocket
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing SpaceX launches and rockets.
 * Provides methods to retrieve launches and rockets,
 * Clean architecture principles are applied to separate domain logic from data sources.
 * This is the domain layer interface that defines the contract for the repository
 */
interface LaunchRepository {
    fun getLaunches(): Flow<List<Launch>>
    suspend fun getLaunchById(id: String): Launch?
    suspend fun getRocketById(id: String): Rocket?
}