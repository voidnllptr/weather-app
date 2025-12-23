package com.example.weatherapp.feature.calculator.domain.repository

interface CalculatorRepository {
    suspend fun calculateExpression(expression: String): String
}