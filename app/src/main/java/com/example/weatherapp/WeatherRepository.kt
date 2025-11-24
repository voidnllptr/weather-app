package com.example.weatherapp

import com.example.weatherapp.data.mapper.WeatherMapper
import com.example.weatherapp.data.remote.RetrofitClient
import com.example.weatherapp.data.remote.WeatherResponse
import com.example.weatherapp.domain.model.WeatherForecast

class WeatherRepository {
    private val apiService = RetrofitClient.weatherApi
    private val mapper = WeatherMapper()

    suspend fun getWeatherForecastByCoordinates(lat: Double, lon: Double): Pair<List<WeatherForecast>, WeatherResponse> {
        val response = apiService.getWeatherForecast(lat, lon)
        val forecasts = mapper.toWeatherForecastList(response.dataseries, response.init)
        return Pair(forecasts, response)
    }
}

