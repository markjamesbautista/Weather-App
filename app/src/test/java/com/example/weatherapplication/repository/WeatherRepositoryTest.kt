package com.example.weatherapplication.repository

import com.example.weatherapplication.api.Response
import com.example.weatherapplication.api.Service
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {

    private lateinit var repository: WeatherRepository
    private val service: Service = mockk()

    @Before
    fun setUp() {
        repository = WeatherRepository(service)
    }

    @Test
    fun `getWeather should call service with correct parameters`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val mockResponse = Response(name = "Test City")
        coEvery { 
            service.getWeather(
                lat = lat,
                long = lon,
                appId = "test",
                unit = "metric"
            ) 
        } returns mockResponse

        // When
        val result = repository.getWeather(lat, lon)

        // Then
        assertEquals(mockResponse, result)
        coVerify { 
            service.getWeather(
                lat = lat,
                long = lon,
                appId = "test",
                unit = "metric"
            ) 
        }
    }
}
