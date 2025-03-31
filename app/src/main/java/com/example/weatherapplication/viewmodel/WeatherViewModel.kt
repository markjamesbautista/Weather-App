package com.example.weatherapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.api.Response
import com.example.weatherapplication.repository.IWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: IWeatherRepository
) : ViewModel() {

    private val _responseModel = MutableStateFlow((listOf<Response>()))
    val responseModel = _responseModel.asStateFlow()

    fun getWeather(lat: Double, long: Double){
        viewModelScope.launch(Dispatchers.IO) {
            _responseModel.value = listOf(repository.getWeather(lat, long))
        }
    }
}