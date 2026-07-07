package com.example.weatherapplication.repository

import com.example.weatherapplication.api.Response
import com.example.weatherapplication.api.Service

class WeatherRepository(
    private val service: Service
) : IWeatherRepository {

    override suspend fun getWeather(lat: Double, long: Double): Response {
        return service.getWeather(
            lat = lat,
            long = long,
            unit = METRIC,
            appId = APP_ID
        )
    }

    companion object {
        private const val APP_ID = "5d16800884cd194ba108308758b66725"
        private const val METRIC = "metric"
    }
}