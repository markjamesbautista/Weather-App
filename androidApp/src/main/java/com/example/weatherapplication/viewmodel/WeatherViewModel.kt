package com.example.weatherapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.api.Response
import com.example.weatherapplication.api.WeatherList
import com.example.weatherapplication.data.WeatherDataStore
import com.example.weatherapplication.data.local.toEntity
import com.example.weatherapplication.repository.IWeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

data class WeatherUiState(
    val weatherList: List<Response> = emptyList(),
    val historyList: List<Response> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: IWeatherRepository,
    private val dataStore: WeatherDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState = _uiState.asStateFlow()
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    init {
        observeRoomHistory()
    }

    private fun observeRoomHistory() {
        repository.getWeatherHistory()
            .onEach { entities ->
                val history = entities.mapNotNull { entity ->
                    try {
                        json.decodeFromString<Response>(entity.rawJson)
                    } catch (e: Exception) {
                        null
                    }
                }
                _uiState.update { it.copy(historyList = history) }
            }
            .launchIn(viewModelScope)
    }

    fun getWeather(lat: Double, long: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = repository.getWeather(lat, long)
                
                // Use current time for dt
                val updatedResult = result.copy(dt = (System.currentTimeMillis() / 1000).toInt())
                val rawJson = json.encodeToString(updatedResult)
                
                // Save to Room
                repository.insertWeatherToHistory(updatedResult.toEntity(rawJson))
                
                // Also save to DataStore as requested
                _uiState.update { state ->
                    val newHistoryList = (listOf(updatedResult) + state.historyList).take(50)
                    viewModelScope.launch {
                        dataStore.saveWeatherHistory(WeatherList(newHistoryList))
                    }

                    state.copy(
                        weatherList = listOf(updatedResult),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Error fetching weather")
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        errorMessage = e.localizedMessage ?: "An unknown error occurred"
                    ) 
                }
            }
        }
    }
    
    fun clearHistory() {
        viewModelScope.launch {
            repository.clearWeatherHistory()
            dataStore.saveWeatherHistory(WeatherList(emptyList()))
        }
    }
}
