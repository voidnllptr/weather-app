package com.example.weatherapp.feature.weather.presentation.viewmodel

sealed class WeatherEvent {
    object LoadWeather : WeatherEvent()
    data class RefreshWeather(val forceUpdate: Boolean = false) : WeatherEvent()
    data class SearchWeather(val query: String) : WeatherEvent()
    object Retry : WeatherEvent()
    object ClearError : WeatherEvent()
}
