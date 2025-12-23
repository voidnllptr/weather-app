package com.example.weatherapp.feature.calculator.presentation.viewmodel

sealed class CalculatorEvent {
    data class Number(val number: Int) : CalculatorEvent()
    data class Operator(val operator: String) : CalculatorEvent()
    object Decimal : CalculatorEvent()
    object Clear : CalculatorEvent()
    object Delete : CalculatorEvent()
    object Calculate : CalculatorEvent()
}