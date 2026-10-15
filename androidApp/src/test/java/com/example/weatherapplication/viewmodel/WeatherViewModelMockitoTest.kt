package com.example.weatherapplication.viewmodel

import com.example.weatherapplication.api.Response
import com.example.weatherapplication.api.WeatherList
import com.example.weatherapplication.data.WeatherDataStore
import com.example.weatherapplication.repository.IWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelMockitoTest {

    private lateinit var viewModel: WeatherViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var repository: IWeatherRepository

    @Mock
    private lateinit var dataStore: WeatherDataStore

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        whenever(repository.getWeatherHistory()).thenReturn(flowOf(emptyList()))
        whenever(dataStore.weatherHistoryFlow).thenReturn(flowOf(WeatherList(emptyList())))

        viewModel = WeatherViewModel(repository, dataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getWeather should update uiState with success`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val mockResponse = Response(name = "Test City")
        whenever(repository.getWeather(lat, lon)).thenReturn(mockResponse)

        // When
        viewModel.getWeather(lat, lon)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals("Test City", state.weatherList.first().name)
        assertEquals(false, state.isLoading)
        verify(repository).getWeather(lat, lon)
    }

    @Test
    fun `getWeather should update uiState with error on failure`() = runTest(testDispatcher) {
        // Given
        val lat = 10.0
        val lon = 20.0
        val errorMessage = "Network Error"
        whenever(repository.getWeather(lat, lon)).thenThrow(RuntimeException(errorMessage))

        // When
        viewModel.getWeather(lat, lon)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals(errorMessage, state.errorMessage)
        assertEquals(false, state.isLoading)
    }
}
