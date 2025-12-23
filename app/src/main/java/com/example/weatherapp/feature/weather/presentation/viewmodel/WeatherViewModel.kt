package com.example.weatherapp.feature.weather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.feature.weather.domain.repository.WeatherRepository
import com.example.weatherapp.feature.weather.presentation.state.WeatherState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.util.Log

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weatherState = MutableStateFlow(WeatherState())
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    init {
        Log.d("WeatherVM", "ViewModel initialized")
        loadWeather()
    }

    fun loadWeather() {
        viewModelScope.launch {
            Log.d("WeatherVM", "loadWeather called")

            _weatherState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                Log.d("WeatherVM", "Fetching data from repository...")
                val forecasts = repository.getWeatherForecast()
                Log.d("WeatherVM", "Success: received ${forecasts.size} forecasts")

                _weatherState.update {
                    WeatherState(
                        forecasts = forecasts,
                        isLoading = false,
                        error = null
                    )
                }

                Log.d("WeatherVM", "State updated with forecasts")

            } catch (e: Exception) {
                Log.e("WeatherVM", "Error loading weather: ${e.message}", e)
                _weatherState.update {
                    WeatherState(
                        forecasts = emptyList(),
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("WeatherVM", "ViewModel cleared")
    }
}