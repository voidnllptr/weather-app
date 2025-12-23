package com.example.weatherapp.core.di

import com.example.weatherapp.feature.auth.di.authModule
import com.example.weatherapp.feature.weather.di.weatherModule
import com.example.weatherapp.feature.calculator.di.calculatorModule
import com.example.weatherapp.feature.list.di.listModule

val appModule = listOf (
    authModule,
    weatherModule,
    calculatorModule,
    listModule
)
