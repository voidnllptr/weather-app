package com.example.weatherapp.feature.calculator.domain.usecase

import com.example.weatherapp.feature.calculator.domain.repository.CalculatorRepository

class CalculateExpressionUseCase(
    private val repository: CalculatorRepository
) {
    suspend operator fun invoke(expression: String): String {
        return repository.calculateExpression(expression)
    }
}