package com.example.weatherapp.data.remote

data class WeatherResponse(
    val init: String,
    val dataseries: List<DataSeries>
)

data class DataSeries(
    val timepoint: Int,
    val cloudcover: Int,
    val lifted_index: Int,
    val prec_type: String,
    val prec_amount: Int,
    val temp2m: Any,
    val rh2m: String,
    val wind10m: Wind10m,
    val weather: String
) {
    fun getTemp2m(): Temp2m {
        return when (temp2m) {
            is Int -> Temp2m(temp2m as Int, temp2m as Int)
            is Double -> Temp2m((temp2m as Double).toInt(), (temp2m as Double).toInt())
            is Temp2m -> temp2m as Temp2m
            else -> Temp2m(0, 0)
        }
    }
}

data class Temp2m(
    val max: Int,
    val min: Int
)

data class Wind10m(
    val direction: String,
    val speed: Int
)
