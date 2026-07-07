package com.example.weatherapplication.modules

import com.example.weatherapplication.repository.IWeatherRepository
import com.example.weatherapplication.repository.WeatherRepository
import org.koin.dsl.module

val repositoryModule = module {
	factory<IWeatherRepository> { WeatherRepository(get()) }
}
