package com.example.weatherapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.api.Response
import com.example.weatherapplication.repository.IWeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: IWeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState = _uiState.asStateFlow()

    fun initHistory(history: List<Response>) {
        _uiState.update { it.copy(historyList = history) }
    }

    fun getWeather(lat: Double, long: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = repository.getWeather(lat, long)
                
                // Create a unique entry using current system time.
                // Using this for BOTH tabs ensures the ViewPager2 detects a change 
                // and refreshes the "Current Weather" display immediately.
                val updatedResult = result.copy(dt = (System.currentTimeMillis() / 1000).toInt())
                
                _uiState.update { state ->
                    val newHistory = (listOf(updatedResult) + state.historyList).take(50)
                    state.copy(
                        weatherList = listOf(updatedResult),
                        historyList = newHistory,
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

data class WeatherUiState(
    val weatherList: List<Response> = emptyList(),
    val historyList: List<Response> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
