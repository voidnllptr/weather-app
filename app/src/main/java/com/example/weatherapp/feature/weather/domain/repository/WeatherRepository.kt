package com.example.weatherapp.feature.weather.domain.repository

import com.example.weatherapp.feature.weather.domain.model.WeatherForecast

interface WeatherRepository {
    suspend fun getWeatherForecast(): List<WeatherForecast>
}