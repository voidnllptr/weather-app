package com.example.weatherapp.feature.weather.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.weatherapp.databinding.FragmentWeatherDetailBinding
import com.example.weatherapp.feature.weather.domain.model.WeatherForecast
import com.example.weatherapp.feature.weather.domain.model.WeatherTypeDisplay
import com.google.android.material.snackbar.Snackbar

class WeatherDetailFragment : Fragment() {

    private var _binding: FragmentWeatherDetailBinding? = null
    private val binding get() = _binding!!
    private var selectedForecast: WeatherForecast? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedForecast = arguments?.getParcelable("selected_forecast")

        if (selectedForecast != null) {
            setupUI()
            bindWeatherData(selectedForecast!!)
        } else {
            showError("Нет данных для отображения")
        }
    }

    private fun setupUI() {
        binding.progressBar.isVisible = false
        binding.weatherDetailContainer.isVisible = false
    }

    @SuppressLint("SetTextI18n")
    private fun bindWeatherData(forecast: WeatherForecast) {
        val weatherDisplay = WeatherTypeDisplay.fromApiString(forecast.weather)

        binding.apply {
            val formattedMaxTemp = forecast.getFormattedMaxTemp()
            val formattedMinTemp = forecast.getFormattedMinTemp()

            val fullDescription = buildString {
                append("Погода: ${weatherDisplay.displayName}\n")
                append("Температура: днем $formattedMaxTemp, ночью $formattedMinTemp\n")
                append("Ветер: ${forecast.windSpeed ?: "N/A"} м/с")
            }

            tvFullDescription.text = fullDescription

            tvDate.text = forecast.formattedDate
            tvDayOfWeek.text = forecast.dayOfWeek
            tvWeather.text = weatherDisplay.displayName

            tvMaxTemp.text = formattedMaxTemp
            tvMinTemp.text = formattedMinTemp

            tvWindSpeed.text = forecast.windSpeed?.let { "$it м/с" } ?: "N/A"

            ivWeatherIcon.setImageResource(weatherDisplay.drawableResId)

            weatherDetailContainer.isVisible = true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showError(message: String) {
        binding.apply {
            progressBar.isVisible = false
            weatherDetailContainer.isVisible = false
            tvFullDescription.isVisible = true
            tvFullDescription.text = message
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}