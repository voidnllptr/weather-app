package com.example.weatherapp.feature.calculator.presentation.state

data class CalculatorState(
    val expression: String = "",
    val result: String = "0",
    val isLoading: Boolean = false,
    val error: String? = null
)