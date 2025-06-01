package com.example.spaceexplorer.di

import android.content.Context
import androidx.room.Room
import com.example.spaceexplorer.data.db.LaunchDao
import com.example.spaceexplorer.data.db.SpaceExplorerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SpaceExplorerDatabase =
        Room.databaseBuilder(context, SpaceExplorerDatabase::class.java, "space_explorer_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideLaunchDao(database: SpaceExplorerDatabase): LaunchDao = database.launchDao()
}
