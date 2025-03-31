package com.example.weatherapplication.api

import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val lat: Double = 0.00,
    val long: Double = 0.00,
    val appid: String = ""
)