package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.domain.model.WeatherForecast

class WeatherActivity : AppCompatActivity() {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var adapter: WeatherAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        setupToolbar()
        initViews()
        setupViewModel()
        setupRecyclerView()
        observeData()

        viewModel.loadWeatherDataByCoordinates(55.7558, 37.6173)
    }

    private fun setupToolbar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.title = getString(R.string.weather_title)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.rvWeather)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = WeatherAdapter { weatherForecast ->
            openWeatherDetail(weatherForecast)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeData() {
        viewModel.weatherForecasts.observe(this) { forecasts ->
            adapter.submitList(forecasts.take(10))
        }

        viewModel.cityInfo.observe(this) { location ->
            supportActionBar?.title = location
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun openWeatherDetail(weatherForecast: WeatherForecast) {
        val intent = Intent(this, WeatherDetailActivity::class.java).apply {
            putExtra("WEATHER_FORECAST", weatherForecast)
        }
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

