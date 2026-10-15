package com.example.weatherapplication.repository

import com.example.weatherapplication.api.Response
import com.example.weatherapplication.data.local.WeatherDao
import com.example.weatherapplication.data.local.WeatherEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class WeatherRepositoryMockitoTest {

    private lateinit var repository: WeatherRepository

    @Mock
    private lateinit var sharedWeatherRepository: SharedWeatherRepository

    @Mock
    private lateinit var weatherDao: WeatherDao

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = WeatherRepository(sharedWeatherRepository, weatherDao)
    }

    @Test
    fun `getWeather should call sharedWeatherRepository with correct parameters`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val mockResponse = Response(name = "Test City")
        whenever(sharedWeatherRepository.getWeather(lat, lon)).thenReturn(mockResponse)

        // When
        val result = repository.getWeather(lat, lon)

        // Then
        assertEquals(mockResponse, result)
        verify(sharedWeatherRepository).getWeather(lat, lon)
    }

    @Test
    fun `getWeatherHistory should call dao`() = runTest {
        // Given
        val mockHistory = listOf(
            WeatherEntity(
                cityId = 1,
                cityName = "City",
                timestamp = 123,
                temperature = 25.0,
                description = "Clear",
                icon = "01d",
                sunrise = 0.0,
                sunset = 0.0,
                country = "US",
                timezone = 0,
                rawJson = "{}"
            )
        )
        whenever(weatherDao.getAllWeatherHistory()).thenReturn(flowOf(mockHistory))

        // When
        val result = repository.getWeatherHistory().first()

        // Then
        assertEquals(mockHistory, result)
        verify(weatherDao).getAllWeatherHistory()
    }

    @Test
    fun `insertWeatherToHistory should call dao insert`() = runTest {
        // Given
        val entity = WeatherEntity(
            cityId = 1,
            cityName = "City",
            timestamp = 123,
            temperature = 25.0,
            description = "Clear",
            icon = "01d",
            sunrise = 0.0,
            sunset = 0.0,
            country = "US",
            timezone = 0,
            rawJson = "{}"
        )

        // When
        repository.insertWeatherToHistory(entity)

        // Then
        verify(weatherDao).insertWeather(entity)
    }

    @Test
    fun `clearWeatherHistory should call dao clear`() = runTest {
        // When
        repository.clearWeatherHistory()

        // Then
        verify(weatherDao).clearHistory()
    }
}
