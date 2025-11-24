package com.example.weatherapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.model.WeatherForecast
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()
    val weatherForecasts = MutableLiveData<List<WeatherForecast>>()
    val cityInfo = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()


    fun loadWeatherDataByCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val (forecasts) = repository.getWeatherForecastByCoordinates(lat, lon)
                weatherForecasts.value = forecasts
                cityInfo.value = "Координаты: $lat, $lon"
            } catch (e: Exception) {
                error.value = "Ошибка загрузки: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }
}

