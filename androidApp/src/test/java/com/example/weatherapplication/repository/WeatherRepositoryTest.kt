package com.example.weatherapplication.repository

import com.example.weatherapplication.api.Response
import com.example.weatherapplication.data.local.WeatherDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {

    private lateinit var repository: WeatherRepository
    private val sharedWeatherRepository: SharedWeatherRepository = mockk()
    private val weatherDao: WeatherDao = mockk()

    @Before
    fun setUp() {
        repository = WeatherRepository(sharedWeatherRepository, weatherDao)
    }

    @Test
    fun `getWeather should call sharedWeatherRepository with correct parameters`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val mockResponse = Response(name = "Test City")
        
        coEvery { 
            sharedWeatherRepository.getWeather(lat, lon) 
        } returns mockResponse

        // When
        val result = repository.getWeather(lat, lon)

        // Then
        assertEquals(mockResponse, result)
        coVerify { 
            sharedWeatherRepository.getWeather(lat, lon) 
        }
    }
}
