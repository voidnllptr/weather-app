package com.example.weatherapp.feature.weather.data.repository

import com.example.weatherapp.feature.weather.data.mapper.WeatherMapper
import com.example.weatherapp.feature.weather.domain.model.WeatherForecast
import com.example.weatherapp.feature.weather.domain.repository.WeatherRepository
import com.example.weatherapp.feature.weather.data.remote.WeatherApi


class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val mapper: WeatherMapper
) : WeatherRepository {

    private companion object {
        const val DEFAULT_LONGITUDE = 37.6173
        const val DEFAULT_LATITUDE = 55.7558
    }

    override suspend fun getWeatherForecast(): List<WeatherForecast> {
        val response = weatherApi.getWeatherForecast(
            longitude = DEFAULT_LONGITUDE,
            latitude = DEFAULT_LATITUDE
        )
        return mapper.mapToWeatherForecast(response)
    }
}

