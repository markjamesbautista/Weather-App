package com.example.weatherapplication.viewmodel

import app.cash.turbine.test
import com.example.weatherapplication.api.Response
import com.example.weatherapplication.repository.IWeatherRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private lateinit var viewModel: WeatherViewModel
    private val repository: IWeatherRepository = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WeatherViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getWeather should update responseModel with repository result`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val mockResponse = Response(name = "Test City")
        coEvery { repository.getWeather(lat, lon) } returns mockResponse

        // When
        viewModel.getWeather(lat, lon)

        // Then
        viewModel.responseModel.test {
            // Turbine will catch the values. 
            // The first value is the initial empty list.
            val initial = awaitItem()
            assertEquals(0, initial.size)

            // The second value should be the one from the repository
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Test City", result[0].name)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
}
