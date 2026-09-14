package com.example.weatherapplication.repository

import com.example.weatherapplication.api.Response
import com.example.weatherapplication.api.Service
import com.example.weatherapplication.data.local.WeatherDao
import com.example.weatherapplication.data.local.WeatherEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val service: Service,
    private val weatherDao: WeatherDao
) : IWeatherRepository {

    override suspend fun getWeather(lat: Double, long: Double): Response {
        return service.getWeather(lat, long)
    }

    override fun getWeatherHistory(): Flow<List<WeatherEntity>> {
        return weatherDao.getAllWeatherHistory()
    }

    override suspend fun insertWeatherToHistory(weather: WeatherEntity) {
        weatherDao.insertWeather(weather)
    }

    override suspend fun deleteWeatherFromHistory(weather: WeatherEntity) {
        weatherDao.deleteWeather(weather)
    }

    override suspend fun clearWeatherHistory() {
        weatherDao.clearHistory()
    }
}