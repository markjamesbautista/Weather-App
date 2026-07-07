package com.example.weatherapplication.api

import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("/data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double
    ): Response

    companion object {
        const val BASE_URL = "https://api.openweathermap.org"
    }
}