package com.example.weatherapplication.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherapplication.api.WeatherList
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "weather_prefs")

@Singleton
class WeatherDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    private val savedWeatherKey = stringPreferencesKey("saved_weather")

    val weatherHistoryFlow: Flow<WeatherList> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val json = preferences[savedWeatherKey] ?: ""
            if (json.isNotEmpty()) {
                try {
                    gson.fromJson(json, WeatherList::class.java)
                } catch (e: Exception) {
                    WeatherList()
                }
            } else {
                WeatherList()
            }
        }

    suspend fun saveWeatherHistory(weatherList: WeatherList) {
        context.dataStore.edit { preferences ->
            preferences[savedWeatherKey] = gson.toJson(weatherList)
        }
    }
}
