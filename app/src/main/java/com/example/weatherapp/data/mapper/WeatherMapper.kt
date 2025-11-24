package com.example.weatherapp.data.mapper

import com.example.weatherapp.data.remote.DataSeries
import com.example.weatherapp.domain.model.WeatherForecast
import java.util.Calendar

class WeatherMapper {

    fun toWeatherForecastList(dataSeries: List<DataSeries>, initDate: String): List<WeatherForecast> {
        val calendar = parseInitDate(initDate)
        return dataSeries.mapIndexed { index, dataSeriesItem ->
            val date = Calendar.getInstance().apply {
                timeInMillis = calendar.timeInMillis
                add(Calendar.HOUR_OF_DAY, dataSeriesItem.timepoint)
            }
            val timestamp = date.timeInMillis / 1000
            WeatherForecast(
                date = timestamp,
                formattedDate = formatDate(date.timeInMillis),
                dayOfWeek = getDayOfWeek(date.timeInMillis),
                weather = dataSeriesItem.weather,
                description = getWeatherDescription(dataSeriesItem.weather),
                maxTemp = dataSeriesItem.getTemp2m().max.toDouble(),
                minTemp = dataSeriesItem.getTemp2m().min.toDouble(),
                temp = ((dataSeriesItem.getTemp2m().max + dataSeriesItem.getTemp2m().min) / 2.0),
                feelsLike = ((dataSeriesItem.getTemp2m().max + dataSeriesItem.getTemp2m().min) / 2.0),
                humidity = dataSeriesItem.rh2m.toString().replace("%", "").toIntOrNull() ?: 0,
                pressure = 0,
                windSpeed = dataSeriesItem.wind10m.speed.toDouble(),
                icon = getWeatherIcon(dataSeriesItem.weather),
                isToday = isToday(date.timeInMillis)
            )
        }
    }

    private fun parseInitDate(initDate: String): Calendar {
        val calendar = Calendar.getInstance()
        if (initDate.length >= 10) {
            val year = initDate.substring(0, 4).toInt()
            val month = initDate.substring(4, 6).toInt() - 1
            val day = initDate.substring(6, 8).toInt()
            val hour = initDate.substring(8, 10).toInt()
            calendar.set(year, month, day, hour, 0, 0)
        }
        return calendar
    }

    private fun getWeatherDescription(weather: String): String {
        return when (weather) {
            "clearday" -> "ясный денечек"
            "clearnight" -> "ясная ночка"
            "clear" -> "ясно"
            "pcloudy" -> "переменная облачность"
            "mcloudy" -> "облачно"
            "cloudy" -> "пасмурно"
            "humid" -> "влажно"
            "lightrain" -> "легкий дождь"
            "rain" -> "дождь"
            "snow" -> "снег"
            "ts" -> "гроза"
            "tsrain" -> "гроза с дождем"
            else -> weather
        }
    }

    private fun getWeatherIcon(weather: String): String {
        return when (weather) {
            "clear" -> "01d"
            "pcloudy" -> "02d"
            "mcloudy" -> "03d"
            "cloudy" -> "04d"
            "humid" -> "50d"
            "lightrain" -> "09d"
            "rain" -> "10d"
            "snow" -> "13d"
            "ts" -> "11d"
            "tsrain" -> "11d"
            else -> "01d"
        }
    }

    private fun formatDate(timestamp: Long): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        return "$day ${getMonthName(month)}"
    }

    private fun getDayOfWeek(timestamp: Long): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Понедельник"
            Calendar.TUESDAY -> "Вторник"
            Calendar.WEDNESDAY -> "Среда"
            Calendar.THURSDAY -> "Четверг"
            Calendar.FRIDAY -> "Пятница"
            Calendar.SATURDAY -> "Суббота"
            Calendar.SUNDAY -> "Воскресенье"
            else -> ""
        }
    }

    private fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "января"
            2 -> "февраля"
            3 -> "марта"
            4 -> "апреля"
            5 -> "мая"
            6 -> "июня"
            7 -> "июля"
            8 -> "августа"
            9 -> "сентября"
            10 -> "октября"
            11 -> "ноября"
            12 -> "декабря"
            else -> ""
        }
    }

    private fun isToday(timestamp: Long): Boolean {
        val today = android.icu.util.Calendar.getInstance()
        val date = android.icu.util.Calendar.getInstance().apply {
            timeInMillis = timestamp
        }
        val year = today.get(android.icu.util.Calendar.YEAR)
        val month = today.get(android.icu.util.Calendar.MONTH) + 1
        val day = today.get(android.icu.util.Calendar.DAY_OF_MONTH)
        val apiYear = date.get(android.icu.util.Calendar.YEAR)
        val apiMonth = date.get(android.icu.util.Calendar.MONTH) + 1
        val apiDay = date.get(android.icu.util.Calendar.DAY_OF_MONTH)
        return year == apiYear && month == apiMonth && day == apiDay
    }
}
