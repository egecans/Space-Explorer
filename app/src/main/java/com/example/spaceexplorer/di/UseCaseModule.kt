package com.example.spaceexplorer.di

import com.example.spaceexplorer.domain.repository.LaunchRepository
import com.example.spaceexplorer.domain.usecase.GetLaunchByIdUseCase
import com.example.spaceexplorer.domain.usecase.GetLaunchesUseCase
import com.example.spaceexplorer.domain.usecase.GetRocketByIdUseCase
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
    fun provideGetLaunchesUseCase(repository: com.example.spaceexplorer.domain.repository.LaunchRepository): com.example.spaceexplorer.domain.usecase.GetLaunchesUseCase {
        return com.example.spaceexplorer.domain.usecase.GetLaunchesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetLaunchByIdUseCase(repository: com.example.spaceexplorer.domain.repository.LaunchRepository): com.example.spaceexplorer.domain.usecase.GetLaunchByIdUseCase {
        return com.example.spaceexplorer.domain.usecase.GetLaunchByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetRocketByIdUseCase(repository: com.example.spaceexplorer.domain.repository.LaunchRepository): com.example.spaceexplorer.domain.usecase.GetRocketByIdUseCase {
        return com.example.spaceexplorer.domain.usecase.GetRocketByIdUseCase(repository)
    }
}
