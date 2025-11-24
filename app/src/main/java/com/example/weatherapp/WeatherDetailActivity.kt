package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.weatherapp.domain.model.WeatherForecast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_detail)

        val weatherForecast = intent.getParcelableExtra<WeatherForecast>("WEATHER_FORECAST")
        
        if (weatherForecast == null) {
            finish()
            return
        }

        setupToolbar()
        displayWeatherDetails(weatherForecast)
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Детали погоды"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @SuppressLint("SetTextI18n")
    private fun displayWeatherDetails(item: WeatherForecast) {
        val dateText: TextView = findViewById(R.id.tvDetailDate)
        val tempText: TextView = findViewById(R.id.tvDetailTemp)
        val descriptionText: TextView = findViewById(R.id.tvDetailDescription)
        val feelsLikeText: TextView = findViewById(R.id.tvDetailFeelsLike)
        val humidityText: TextView = findViewById(R.id.tvDetailHumidity)
        val pressureText: TextView = findViewById(R.id.tvDetailPressure)
        val weatherIcon: ImageView = findViewById(R.id.ivDetailWeatherIcon)

        val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        dateText.text = dateFormat.format(Date(item.date * 1000))

        tempText.text = "${item.temp.toInt()}°C"
        val description = item.description
        descriptionText.text = description.replaceFirstChar { 
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
        }
        
        feelsLikeText.text = getString(R.string.feels_like) + ": ${item.feelsLike.toInt()}°C"
        humidityText.text = getString(R.string.humidity) + ": ${item.humidity}%"
        pressureText.text = getString(R.string.pressure) + ": ${item.pressure} мм рт. ст."

        weatherIcon.visibility = android.view.View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

