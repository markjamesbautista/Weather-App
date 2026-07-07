package com.example.weatherapplication.modules

import com.example.weatherapplication.repository.IWeatherRepository
import com.example.weatherapplication.repository.WeatherRepository
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
    abstract fun bindWeatherRepository(
        weatherRepository: WeatherRepository
    ): IWeatherRepository
}
