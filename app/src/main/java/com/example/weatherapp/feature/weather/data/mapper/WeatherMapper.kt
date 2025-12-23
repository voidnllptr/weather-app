package com.example.weatherapp.feature.weather.data.mapper

import com.example.weatherapp.feature.weather.data.remote.DataSeries
import com.example.weatherapp.feature.weather.data.remote.WeatherResponse
import com.example.weatherapp.feature.weather.domain.model.WeatherForecast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object WeatherMapper {

    fun mapToWeatherForecast(response: WeatherResponse): List<WeatherForecast> {
        return response.dataseries.mapIndexed { index, dataSeries ->
            mapDataSeriesToWeatherForecast(dataSeries, index)
        }
    }

    private fun mapDataSeriesToWeatherForecast(dataSeries: DataSeries, index: Int): WeatherForecast {
        val date = parseDate(dataSeries.date)
        val formattedDate = formatDate(date)
        val dayOfWeek = getDayOfWeek(date)
        val isToday = index == 0

        return WeatherForecast(
            date = dataSeries.date,
            formattedDate = formattedDate,
            dayOfWeek = dayOfWeek,
            weather = dataSeries.weather,
            maxTemp = dataSeries.temp2m.max ?: 0,
            minTemp = dataSeries.temp2m.min ?: 0,
            windSpeed = dataSeries.wind10m_max,
            isToday = isToday
        )
    }

    private fun parseDate(dateInt: Int): Date {
        val dateString = dateInt.toString()
        return try {
            SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(dateString) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }

    private fun formatDate(date: Date): String {
        val dayFormat = SimpleDateFormat("d", Locale.getDefault())
        val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        val day = dayFormat.format(date)
        val month = monthFormat.format(date).lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        return "$day $month"
    }

    private fun getDayOfWeek(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date

        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Понедельник"
            Calendar.TUESDAY -> "Вторник"
            Calendar.WEDNESDAY -> "Среда"
            Calendar.THURSDAY -> "Четверг"
            Calendar.FRIDAY -> "Пятница"
            Calendar.SATURDAY -> "Суббота"
            Calendar.SUNDAY -> "Воскресенье"
            else -> "Неизвестный день недели"
        }
    }
}