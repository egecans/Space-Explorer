package com.example.spaceexplorer.di

import android.content.Context
import com.example.spaceexplorer.data.api.SpaceXApiService
import com.example.spaceexplorer.data.db.SpaceExplorerDatabase
import com.example.spaceexplorer.data.repository.LaunchRepositoryImpl
import com.example.spaceexplorer.domain.repository.LaunchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLaunchRepository(
        apiService: SpaceXApiService,
        database: SpaceExplorerDatabase,
        @ApplicationContext context: Context
    ): LaunchRepository {
        return LaunchRepositoryImpl(apiService, database, context)
    }
}
