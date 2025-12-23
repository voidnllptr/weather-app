package com.example.weatherapp.feature.weather.di

import com.example.weatherapp.core.remote.RetrofitClient
import com.example.weatherapp.feature.weather.data.mapper.WeatherMapper
import com.example.weatherapp.feature.weather.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.feature.weather.domain.repository.WeatherRepository
import com.example.weatherapp.feature.weather.presentation.viewmodel.WeatherViewModel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val weatherModule = module {
    single { RetrofitClient.weatherApi }
    single<WeatherMapper> { WeatherMapper }
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get()) }
    viewModel { WeatherViewModel(get()) }
}
