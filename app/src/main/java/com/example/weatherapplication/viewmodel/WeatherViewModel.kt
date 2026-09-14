package com.example.weatherapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.api.Response
import com.example.weatherapplication.api.WeatherList
import com.example.weatherapplication.data.WeatherDataStore
import com.example.weatherapplication.data.local.toEntity
import com.example.weatherapplication.repository.IWeatherRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
    private val dataStore: WeatherDataStore,
    private val gson: Gson
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Load history from Room
        observeRoomHistory()
        // We still keep DataStore for simple prefs or other lightweight data if needed,
        // but here we demonstrate using both by loading initial status from DataStore
        // or keeping them in sync. For this task, we will load Room history into historyList.
    }

    private fun observeRoomHistory() {
        repository.getWeatherHistory()
            .onEach { entities ->
                val history = entities.mapNotNull { entity ->
                    try {
                        gson.fromJson(entity.rawJson, Response::class.java)
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
                val rawJson = gson.toJson(updatedResult)
                
                // Save to Room
                repository.insertWeatherToHistory(updatedResult.toEntity(rawJson))
                
                // Also save to DataStore as requested (demonstrating both)
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
