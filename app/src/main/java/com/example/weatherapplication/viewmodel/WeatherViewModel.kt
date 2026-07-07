package com.example.weatherapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.api.Response
import com.example.weatherapplication.api.WeatherList
import com.example.weatherapplication.data.WeatherDataStore
import com.example.weatherapplication.repository.IWeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
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
    private val dataStore: WeatherDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Automatically load history from DataStore on startup
        observeHistory()
    }

    private fun observeHistory() {
        viewModelScope.launch {
            dataStore.weatherHistoryFlow.collectLatest { history ->
                _uiState.update { it.copy(historyList = history.list) }
            }
        }
    }

    fun getWeather(lat: Double, long: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = repository.getWeather(lat, long)
                
                // Use current time for dt to ensure BOTH current weather and history 
                // detect a change on every refresh, triggering the UI to update.
                val updatedResult = result.copy(dt = (System.currentTimeMillis() / 1000).toInt())
                
                _uiState.update { state ->
                    val newHistoryList = (listOf(updatedResult) + state.historyList).take(50)
                    
                    // Save to DataStore asynchronously
                    viewModelScope.launch {
                        dataStore.saveWeatherHistory(WeatherList(newHistoryList))
                    }

                    state.copy(
                        weatherList = listOf(updatedResult),
                        historyList = newHistoryList,
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
}
