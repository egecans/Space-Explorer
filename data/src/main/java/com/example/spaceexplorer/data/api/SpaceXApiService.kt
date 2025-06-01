package com.example.spaceexplorer.data.api

import com.example.spaceexplorer.data.model.LaunchDto
import com.example.spaceexplorer.data.model.RocketDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit service interface for SpaceX API.
 *
 * This interface defines the network API calls to fetch SpaceX data.
 * It is part of the data layer in Clean Architecture, responsible
 * for fetching raw data from remote sources.
 */
interface SpaceXApiService {

    /**
     * Fetches the list of all launches.
     *
     * @return List of [LaunchDto] representing launches from the API.
     */
    @GET("launches")
    suspend fun getLaunches(): List<LaunchDto>

    /**
     * Fetches the launch details for a given launch ID.
     *
     * @param id Launch ID to fetch.
     * @return [LaunchDto] for the specified launch, or null if not found.
     */
    @GET("launches/{id}")
    suspend fun getLaunchById(@Path("id") id: String): LaunchDto?

    /**
     * Fetches the list of all rockets.
     *
     * @return List of [RocketDto] representing rockets from the API.
     */
    @GET("rockets")
    suspend fun getRockets(): List<RocketDto>

    /**
     * Fetches the rocket details for a given rocket ID.
     *
     * @param id Rocket ID to fetch.
     * @return [RocketDto] for the specified rocket, or null if not found.
     */
    @GET("rockets/{id}")
    suspend fun getRocketById(@Path("id") id: String): RocketDto?
}
