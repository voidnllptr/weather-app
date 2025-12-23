package com.example.weatherapp.feature.weather.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class WeatherForecast(
    val date: Int,
    val formattedDate: String,
    val dayOfWeek: String,
    val weather: String,
    val maxTemp: Int,
    val minTemp: Int,
    val windSpeed: Int?,
    val isToday: Boolean = false
) : Parcelable {
    fun getFormattedMaxTemp(): String {
        return if (maxTemp == -9999) "—" else "$maxTemp°"
    }

    fun getFormattedMinTemp(): String {
        return if (minTemp == -9999) "—" else "$minTemp°"
    }
}