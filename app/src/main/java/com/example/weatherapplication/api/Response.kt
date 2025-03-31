package com.example.weatherapplication.api

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val coord: Coord = Coord(),
    val main: Main = Main(),
    val clouds: Clouds = Clouds(),
    val wind: Wind = Wind(),
    val sys: Sys = Sys(),
    val weather: List<Weather> = emptyList(),
    val base: String = "",
    val visibility: Int = 0,
    val dt: Int = 0,
    val timezone: Int = 0,
    val id: Int = 0,
    val cod: Int = 0,
    val name: String = "",

) {
    @Serializable
    data class Coord(
        val long: Double= 0.00,
        val lat: Double= 0.00,
    )

    @Serializable
    data class Weather(
        val id: Double= 0.00,
        val main: String = "",
        val description: String = "",
        val icon: String = "",
    )

    @Serializable
    data class Main(
        val temp: Double = 0.00,
        val feels_like: Double= 0.00,
        val temp_min: Double= 0.00,
        val temp_max: Double= 0.00,
        val pressure: Double= 0.00,
        val humidity: Double= 0.00,
        val sea_level: Double= 0.00,
        val grnd_level: Double= 0.00,
    )

    @Serializable
    data class Wind(
        val speed: Double= 0.00,
        val deg: Double= 0.00,
        val gust: Double= 0.00,
    )

    @Serializable
    data class Clouds(
        val all: Double= 0.00,
    )
    @Serializable
    data class Sys(
        val country: String = "",
        val sunrise: Double = 0.00,
        val sunset: Double = 0.00,
    )
}