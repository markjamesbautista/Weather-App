package com.example.weatherapplication.repository

import com.example.weatherapplication.api.Response
import com.example.weatherapplication.api.WeatherApiClient

class SharedWeatherRepository(
    private val apiClient: WeatherApiClient = WeatherApiClient()
) {
    suspend fun getWeather(lat: Double, long: Double): Response {
        return apiClient.getWeather(lat, long)
    }
}
