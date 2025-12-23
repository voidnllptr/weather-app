package com.example.weatherapp.feature.calculator.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.feature.calculator.domain.usecase.CalculateExpressionUseCase
import com.example.weatherapp.feature.calculator.presentation.state.CalculatorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalculatorViewModel(
    private val calculateExpressionUseCase: CalculateExpressionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()

    fun onEvent(event: CalculatorEvent) {
        when (event) {
            is CalculatorEvent.Number -> addNumber(event.number)
            is CalculatorEvent.Operator -> addOperator(event.operator)
            CalculatorEvent.Decimal -> addDecimal()
            CalculatorEvent.Clear -> clear()
            CalculatorEvent.Delete -> delete()
            CalculatorEvent.Calculate -> calculate()
        }
    }

    private fun addNumber(number: Int) {
        _state.update { currentState ->
            currentState.copy(
                expression = currentState.expression + number,
                error = null
            )
        }
    }

    private fun addOperator(operator: String) {
        val lastChar = _state.value.expression.lastOrNull()

        _state.update { currentState ->
            if (lastChar?.isDigit() == false && lastChar != '.') {
                currentState.copy(
                    expression = currentState.expression.dropLast(1) + operator,
                    error = null
                )
            } else {
                currentState.copy(
                    expression = currentState.expression + operator,
                    error = null
                )
            }
        }
    }

    private fun addDecimal() {
        val expression = _state.value.expression
        val lastNumber = expression.takeLastWhile { it.isDigit() || it == '.' }

        if (!lastNumber.contains('.')) {
            if (expression.isEmpty() || !expression.last().isDigit()) {
                _state.update {
                    it.copy(expression = expression + "0.", error = null)
                }
            } else {
                _state.update {
                    it.copy(expression = "$expression.", error = null)
                }
            }
        }
    }

    private fun clear() {
        _state.update { CalculatorState() }
    }

    private fun delete() {
        _state.update { currentState ->
            currentState.copy(
                expression = currentState.expression.dropLast(1),
                error = null
            )
        }
    }

    private fun calculate() {
        val expression = _state.value.expression

        if (expression.isEmpty()) return

        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val result = calculateExpressionUseCase(expression)

                _state.update {
                    it.copy(
                        expression = result,
                        result = result,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message ?: "Ошибка вычисления",
                        isLoading = false
                    )
                }
            }
        }
    }
}