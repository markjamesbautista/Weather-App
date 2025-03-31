package com.example.weatherapplication.repository

import com.example.weatherapplication.api.Response

interface IWeatherRepository {

    suspend fun getWeather(lat: Double, long: Double): Response
}