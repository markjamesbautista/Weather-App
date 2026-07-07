package com.example.weatherapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.api.Response
import com.example.weatherapplication.repository.IWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class WeatherViewModel(
    private val repository: IWeatherRepository
) : ViewModel() {

    private val _responseModel = MutableStateFlow((listOf<Response>()))
    val responseModel = _responseModel.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun getWeather(lat: Double, long: Double){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _error.value = null
                val result = repository.getWeather(lat, long)
                _responseModel.value = listOf(result)
            } catch (e: Exception) {
                Timber.e(e, "Error fetching weather")
                _error.value = e.localizedMessage ?: "An unknown error occurred"
            }
        }
    }
}