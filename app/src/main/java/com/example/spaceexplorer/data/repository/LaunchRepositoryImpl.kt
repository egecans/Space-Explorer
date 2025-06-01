package com.example.spaceexplorer.data.repository

import android.content.Context
import android.util.Log
import com.example.spaceexplorer.common.NetworkUtils
import com.example.spaceexplorer.common.error.NoInternetException
import com.example.spaceexplorer.data.api.SpaceXApiService
import com.example.spaceexplorer.data.mapper.toDomain
import com.example.spaceexplorer.domain.model.Launch
import com.example.spaceexplorer.domain.model.Rocket
import com.example.spaceexplorer.domain.repository.LaunchRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Concrete implementation of [LaunchRepository].
 *
 * This class lives in the data layer and is responsible for fetching data
 * from the [SpaceXApiService] remote data source and mapping API models
 * to domain models.
 *
 * By implementing the domain interface, this class follows the Dependency Rule:
 * the domain layer depends only on abstractions (interfaces),
 * and this implementation depends on data-layer classes.
 */
class LaunchRepositoryImpl @Inject constructor(
    private val apiService: SpaceXApiService,
    @ApplicationContext private val context: Context
) : LaunchRepository {

    /**
     * Fetches all launches and their corresponding rockets,
     * combines them into domain [Launch] models with rocket names.
     */
    override fun getLaunches(): Flow<List<Launch>> = flow {

        // check Internet connection at first
        if (!NetworkUtils.isNetworkAvailable(context)) {
            throw NoInternetException("No internet connection")
        }

        val launchesDto = apiService.getLaunches()
        val rocketsDto = apiService.getRockets()

        val rocketMap = rocketsDto.associateBy { it.id } // RocketID, RocketDto
        Log.i("LaunchRepository", "Fetched ${rocketsDto.size} rockets and mapped with ID")

        val launches = launchesDto.map { launchDto ->
            val rocketName = rocketMap[launchDto.rocket]?.name ?: "Unknown Rocket" // if rocket not found
            launchDto.toDomain(rocketName)
        }
        Log.i("LaunchRepository", "Mapped ${launches.size} launches with rocket names")
        emit(launches)
    }

    /**
     * Fetches detailed launch info by ID along with its rocket.
     */
    override suspend fun getLaunchById(id: String): Launch? {
        val launchDto = apiService.getLaunchById(id) ?: return null
        val rocketDto = apiService.getRocketById(launchDto.rocket) ?: return null
        return launchDto.toDomain(rocketDto.name)
    }

    /**
     * Fetches rocket info by ID.
     */
    override suspend fun getRocketById(id: String): Rocket? {
        val rocketDto = apiService.getRocketById(id) ?: return null
        return rocketDto.toDomain()
    }
}
