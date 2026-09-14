package com.example.weatherapplication.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_history ORDER BY timestamp DESC")
    fun getAllWeatherHistory(): Flow<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Delete
    suspend fun deleteWeather(weather: WeatherEntity)

    @Query("DELETE FROM weather_history")
    suspend fun clearHistory()
}
