package com.example.weatherapplication.repository

import com.example.weatherapplication.api.Response
import com.example.weatherapplication.api.Service
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val service: Service
) : IWeatherRepository {

    override suspend fun getWeather(lat: Double, long: Double): Response {
        return service.getWeather(lat, long)
    }
}