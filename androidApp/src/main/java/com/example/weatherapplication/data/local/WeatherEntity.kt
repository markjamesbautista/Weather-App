package com.example.weatherapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapplication.api.Response

@Entity(tableName = "weather_history")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,
    val cityId: Int,
    val cityName: String,
    val timestamp: Int,
    val temperature: Double,
    val description: String,
    val icon: String,
    val sunrise: Double,
    val sunset: Double,
    val country: String,
    val timezone: Int,
    // Store the full response as well if needed, or just specific fields
    val rawJson: String 
)

fun Response.toEntity(rawJson: String): WeatherEntity {
    return WeatherEntity(
        cityId = id,
        cityName = name,
        timestamp = dt,
        temperature = main.temp,
        description = weather.firstOrNull()?.description ?: "",
        icon = weather.firstOrNull()?.icon ?: "",
        sunrise = sys.sunrise,
        sunset = sys.sunset,
        country = sys.country,
        timezone = timezone,
        rawJson = rawJson
    )
}
