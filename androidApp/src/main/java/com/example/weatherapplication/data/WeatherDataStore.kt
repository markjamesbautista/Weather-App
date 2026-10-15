package com.example.weatherapplication.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherapplication.api.WeatherList
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "weather_prefs")

@Singleton
class WeatherDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val savedWeatherKey = stringPreferencesKey("saved_weather")
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    val weatherHistoryFlow: Flow<WeatherList> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val jsonString = preferences[savedWeatherKey] ?: ""
            if (jsonString.isNotEmpty()) {
                try {
                    json.decodeFromString<WeatherList>(jsonString)
                } catch (e: Exception) {
                    WeatherList()
                }
            } else {
                WeatherList()
            }
        }

    suspend fun saveWeatherHistory(weatherList: WeatherList) {
        context.dataStore.edit { preferences ->
            preferences[savedWeatherKey] = json.encodeToString(weatherList)
        }
    }
}
