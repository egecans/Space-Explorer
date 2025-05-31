package com.example.spaceexplorer.di

import com.example.spaceexplorer.domain.repository.LaunchRepository
import com.example.spaceexplorer.domain.usecase.GetLaunchByIdUseCase
import com.example.spaceexplorer.domain.usecase.GetLaunchesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetLaunchesUseCase(repository: LaunchRepository): GetLaunchesUseCase {
        return GetLaunchesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetLaunchByIdUseCase(repository: LaunchRepository): GetLaunchByIdUseCase {
        return GetLaunchByIdUseCase(repository)
    }
}
