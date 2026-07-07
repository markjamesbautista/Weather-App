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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
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
    fun `getWeather should update uiState with result on success`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val mockResponse = Response(name = "Test City")
        coEvery { repository.getWeather(lat, lon) } returns mockResponse

        // Then
        viewModel.uiState.test {
            // Initial state
            val initialState = awaitItem()
            assertEquals(0, initialState.weatherList.size)
            assertFalse(initialState.isLoading)
            assertNull(initialState.errorMessage)

            // When
            viewModel.getWeather(lat, lon)

            // Skip loading state if using UnconfinedTestDispatcher it might emit quickly
            var state = awaitItem()
            if (state.isLoading) {
                state = awaitItem()
            }

            assertEquals(1, state.weatherList.size)
            assertEquals("Test City", state.weatherList[0].name)
            assertFalse(state.isLoading)
            assertNull(state.errorMessage)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getWeather should update uiState with error on failure`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val errorMessage = "Network Error"
        coEvery { repository.getWeather(lat, lon) } throws Exception(errorMessage)

        // Then
        viewModel.uiState.test {
            awaitItem() // Initial

            // When
            viewModel.getWeather(lat, lon)

            var state = awaitItem()
            if (state.isLoading) {
                state = awaitItem()
            }

            assertEquals(0, state.weatherList.size)
            assertFalse(state.isLoading)
            assertEquals(errorMessage, state.errorMessage)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
}
