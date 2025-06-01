package com.example.spaceexplorer.data.repository

import android.content.Context
import android.util.Log
import com.example.spaceexplorer.common.NetworkUtils
import com.example.spaceexplorer.common.error.NoInternetException
import com.example.spaceexplorer.data.api.SpaceXApiService
import com.example.spaceexplorer.data.db.SpaceExplorerDatabase
import com.example.spaceexplorer.data.mapper.toDomain
import com.example.spaceexplorer.data.mapper.toEntity
import com.example.spaceexplorer.domain.model.Launch
import com.example.spaceexplorer.domain.model.Rocket
import com.example.spaceexplorer.domain.repository.LaunchRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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
    private val database: SpaceExplorerDatabase,
    @ApplicationContext private val context: Context
) : LaunchRepository {

    private val launchDao = database.launchDao()

    /**
     * Fetches all launches and their corresponding rockets,
     * combines them into domain [Launch] models with rocket names.
     */
    override fun getLaunches(): Flow<List<Launch>> = flow {
        // Check network availability before making API calls
        if (NetworkUtils.isNetworkAvailable(context)){
            Log.i("LaunchRepository", "Network available, fetching launches and rockets from API")
            val launchesDto = apiService.getLaunches()
            val rocketsDto = apiService.getRockets()

            val rocketMap = rocketsDto.associateBy { it.id } // RocketID, RocketDto
            Log.i("LaunchRepository", "Fetched ${rocketsDto.size} rockets and mapped with ID")

            val launches = launchesDto.map { launchDto ->
                val rocketName = rocketMap[launchDto.rocket]?.name ?: "Unknown Rocket" // if rocket not found
                launchDto.toDomain(rocketName)
            }
            Log.i("LaunchRepository", "Mapped ${launches.size} launches with rocket names")

            // Check if already cached to avoid duplicate insert
            val prefs = context.getSharedPreferences("launch_prefs", Context.MODE_PRIVATE)
            val cached = prefs.getBoolean("launches_cached", false)
            if (!cached) {
                launchDao.insertLaunches(launches.map { it.toEntity() })
                launchDao.insertRockets(rocketsDto.map { it.toEntity() })
                prefs.edit().putBoolean("launches_cached", true).apply()
            }

            emit(launches)
        } else {
            Log.w("LaunchRepository", "No network available, fetching cached launches")

            // Offline mode — emit cached launches if available
            val cachedLaunches: Flow<List<Launch>> = launchDao.getLaunches()
                .map { list -> list.map { it.toDomain(it.rocketName) } }
            // it gets list in flow, then map it to domain model with rocket name

            val cachedLaunchesList = cachedLaunches.firstOrNull()
            Log.i("LaunchRepository", "Cached launches count: ${cachedLaunchesList?.size ?: 0}")

            if (cachedLaunchesList.isNullOrEmpty()) {
                Log.i("LaunchRepository", "No cached launches available")
                throw NoInternetException("No internet and no cached data available")
            }
            emit(cachedLaunchesList)
        }
    }

    /**
     * Fetches detailed launch info by ID along with its rocket.
     */
    override suspend fun getLaunchById(id: String): Launch? {
        if (NetworkUtils.isNetworkAvailable(context)) {
            Log.i("LaunchRepository", "Network available, fetching launch by ID: $id")
            val launchDto = apiService.getLaunchById(id) ?: return null
            val rocketDto = apiService.getRocketById(launchDto.rocket) ?: return null
            val launch = launchDto.toDomain(rocketDto.name)
            return launch
        } else {
            Log.i("LaunchRepository", "No network available, fetching cached launch by ID: $id")
            val launchEntity = launchDao.getLaunchById(id) ?: return null
            return launchEntity.toDomain(launchEntity.rocketName)
        }
    }



    /**
     * Fetches rocket info by ID.
     */
    override suspend fun getRocketById(id: String): Rocket? {
        if (NetworkUtils.isNetworkAvailable(context)) {
            Log.i("LaunchRepository", "Network available, fetching rocket by ID: $id")
            val rocketDto = apiService.getRocketById(id) ?: return null
            return rocketDto.toDomain()
        } else{
            Log.i("LaunchRepository", "No network available, fetching cached rocket by ID: $id")
            val rocketEntity = launchDao.getRocketById(id) ?: return null
            return rocketEntity.toDomain()
        }
    }
}
