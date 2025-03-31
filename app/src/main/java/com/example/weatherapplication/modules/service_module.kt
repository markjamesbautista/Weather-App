package com.example.weatherapplication.modules

import com.example.weatherapplication.api.Service
import com.example.weatherapplication.viewmodel.WeatherViewModel
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(ExperimentalSerializationApi::class)
val serviceModule = module {
	single { Service() }
}
