package com.example.weatherapplication.api

import kotlinx.serialization.Serializable

@Serializable
data class WeatherList(
    val list: List<Response> = emptyList()
)