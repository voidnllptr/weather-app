package com.example.weatherapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherForecast(
    val date: Long,
    val formattedDate: String,
    val dayOfWeek: String,
    val weather: String,
    val description: String,
    val maxTemp: Double,
    val minTemp: Double,
    val temp: Double,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val icon: String,
    val isToday: Boolean
) : Parcelable

