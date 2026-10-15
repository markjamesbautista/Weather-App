package com.example.weatherapplication.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class WeatherApiClient(
    private val apiKey: String = "your_api_key",
    private val units: String = "metric"
) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.BODY
        }
    }

    suspend fun getWeather(lat: Double, long: Double): Response {
        return client.get("https://api.openweathermap.org/data/2.5/weather") {
            parameter("lat", lat)
            parameter("lon", long)
            parameter("appid", apiKey)
            parameter("units", units)
        }.body()
    }
}
