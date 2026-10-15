package com.example.weatherapplication.repository

import com.example.weatherapplication.api.Response
import com.example.weatherapplication.data.local.WeatherEntity
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {

    suspend fun getWeather(lat: Double, long: Double): Response
    
    // Room operations
    fun getWeatherHistory(): Flow<List<WeatherEntity>>
    suspend fun insertWeatherToHistory(weather: WeatherEntity)
    suspend fun deleteWeatherFromHistory(weather: WeatherEntity)
    suspend fun clearWeatherHistory()
}