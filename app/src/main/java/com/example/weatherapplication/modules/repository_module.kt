package com.example.weatherapplication.modules

import com.example.weatherapplication.api.Service
import com.example.weatherapplication.repository.IWeatherRepository
import com.example.weatherapplication.repository.WeatherRepository
import com.example.weatherapplication.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
	factory<IWeatherRepository> { WeatherRepository(get()) }
}
