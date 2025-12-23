package com.example.weatherapp.feature.weather.presentation.state

import com.example.weatherapp.feature.weather.domain.model.WeatherForecast

data class WeatherState(
    val forecasts: List<WeatherForecast> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)