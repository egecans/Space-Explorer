package com.example.spaceexplorer.di

import com.example.spaceexplorer.data.repository.LaunchRepositoryImpl
import com.example.spaceexplorer.domain.repository.LaunchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLaunchRepository(
        launchRepositoryImpl: LaunchRepositoryImpl
    ): LaunchRepository
}
