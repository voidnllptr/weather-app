package com.example.weatherapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("bin/api.pl")
    suspend fun getWeatherForecast(
        @Query("lon") lon: Double,
        @Query("lat") lat: Double,
        @Query("product") product: String = "civil",
        @Query("output") output: String = "json",
        @Query("unit") unit: String = "metric"
    ): WeatherResponse
}

