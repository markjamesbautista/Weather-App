package com.example.weatherapplication.modules

import com.example.weatherapplication.repository.SharedWeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSharedWeatherRepository(): SharedWeatherRepository {
        return SharedWeatherRepository()
    }
}
