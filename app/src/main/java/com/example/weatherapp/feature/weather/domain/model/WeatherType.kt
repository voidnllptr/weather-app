package com.example.weatherapp.feature.weather.domain.model
import com.example.weatherapp.R

enum class WeatherTypeDisplay(
    val displayName: String,
    val apiStrings: List<String>,
    val emoji: String,
    val drawableResId: Int
) {
    SUNNY(
        displayName = "Солнечно",
        apiStrings = listOf("clear"),
        emoji = "☀️",
        drawableResId = R.drawable.ic_sunny
    ),
    PARTLY_CLOUDY(
        displayName = "Переменная облачность",
        apiStrings = listOf("pcloudy"),
        emoji = "⛅",
        drawableResId = R.drawable.ic_partly_cloudy
    ),
    CLOUDY(
        displayName = "Облачно",
        apiStrings = listOf("mcloudy", "cloudy"),
        emoji = "☁️",
        drawableResId = R.drawable.ic_cloudy
    ),
    FOGGY(
        displayName = "Туманно",
        apiStrings = listOf("fog", "humid"),
        emoji = "🌫️",
        drawableResId = R.drawable.ic_fog
    ),
    LIGHT_RAIN(
        displayName = "Небольшой дождь",
        apiStrings = listOf("lightrain", "oshower", "ishower"),
        emoji = "🌦️",
        drawableResId = R.drawable.ic_light_rain
    ),
    RAINY(
        displayName = "Дождь",
        apiStrings = listOf("rain"),
        emoji = "🌧️",
        drawableResId = R.drawable.ic_rain
    ),
    SNOWY(
        displayName = "Снег",
        apiStrings = listOf("snow", "lightsnow"),
        emoji = "❄️",
        drawableResId = R.drawable.ic_snow
    ),
    THUNDERSTORM(
        displayName = "Гроза",
        apiStrings = listOf("ts", "tsrain"),
        emoji = "⛈️",
        drawableResId = R.drawable.ic_thunderstorm
    ),
    WINDY(
        displayName = "Ветрено",
        apiStrings = listOf("windy"),
        emoji = "💨",
        drawableResId = R.drawable.ic_windy
    ),
    UNKNOWN(
        displayName = "Неизвестно",
        apiStrings = emptyList(),
        emoji = "❓",
        drawableResId = R.drawable.ic_unknown
    );
    companion object {
        fun fromApiString(apiWeather: String): WeatherTypeDisplay {
            val normalizedApiWeather = apiWeather.lowercase().trim()

            return WeatherTypeDisplay.entries.firstOrNull { weatherType ->
                weatherType.apiStrings.any { it.equals(normalizedApiWeather, ignoreCase = true) }
            } ?: UNKNOWN
        }
    }
}