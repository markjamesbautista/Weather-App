package com.example.weatherapplication.modules

import com.example.weatherapplication.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
	viewModel {
		WeatherViewModel(get())
	}
}
