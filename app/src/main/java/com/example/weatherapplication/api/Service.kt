package com.example.weatherapplication.api

import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Service {
    @GET("/data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") appId: String,
        @Query("units") unit: String
    ): Response

    companion object {
        private const val BASE_URL ="https://api.openweathermap.org"
        @ExperimentalSerializationApi
        operator fun invoke(): Service {
            val okHttpClient = OkHttpClient.Builder()
            okHttpClient.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Service::class.java)
        }
    }
}