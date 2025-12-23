package com.example.weatherapp.feature.calculator.data.repository

import com.example.weatherapp.feature.calculator.domain.repository.CalculatorRepository
import kotlin.math.abs

class CalculatorRepositoryImpl : CalculatorRepository {

    override suspend fun calculateExpression(expression: String): String {
        val cleanExpr = expression.trim().replace(" ", "")

        if (cleanExpr.isEmpty()) {
            return "0"
        }

        try {
            val result = evaluateWithStack(cleanExpr)
            return formatResult(result)
        } catch (e: IllegalArgumentException) {
            throw e
        } catch (e: Exception) {
            throw IllegalArgumentException("Некорректное выражение")
        }
    }

    private fun evaluateWithStack(expr: String): Double {
        val numbers = mutableListOf<Double>()
        val operators = mutableListOf<Char>()
        var i = 0

        while (i < expr.length) {
            val ch = expr[i]

            when {
                ch.isDigit() || ch == '.' -> {
                    val numberResult = parseFullNumber(expr, i)
                    numbers.add(numberResult.value)
                    i = numberResult.index
                }
                ch == '-' && (i == 0 || expr[i-1] in "+-*/") -> {
                    val numberResult = parseFullNumber(expr, i)
                    numbers.add(numberResult.value)
                    i = numberResult.index
                }
                ch in "+-*/" -> {
                    while (operators.isNotEmpty() &&
                        precedence(operators.last()) >= precedence(ch)) {
                        applyOperation(numbers, operators)
                    }
                    operators.add(ch)
                    i++
                }
                else -> {
                    throw IllegalArgumentException("Недопустимый символ: '$ch'")
                }
            }
        }

        while (operators.isNotEmpty()) {
            applyOperation(numbers, operators)
        }

        if (numbers.size != 1) {
            throw IllegalArgumentException("Некорректное выражение")
        }

        return numbers[0]
    }

    private data class ParsedNumber(val value: Double, val index: Int)

    private fun parseFullNumber(expr: String, startIndex: Int): ParsedNumber {
        var i = startIndex
        val sb = StringBuilder()

        if (i < expr.length && expr[i] == '-') {
            sb.append('-')
            i++
        }

        var hasDigit = false
        var hasDot = false

        while (i < expr.length) {
            val ch = expr[i]
            when {
                ch.isDigit() -> {
                    sb.append(ch)
                    hasDigit = true
                    i++
                }
                ch == '.' && !hasDot -> {
                    sb.append(ch)
                    hasDot = true
                    i++
                }
                else -> break
            }
        }

        if (!hasDigit) {
            throw IllegalArgumentException("Ожидалось число")
        }

        return try {
            ParsedNumber(sb.toString().toDouble(), i)
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Некорректное число: '${sb}'")
        }
    }

    private fun precedence(op: Char): Int {
        return when (op) {
            '+', '-' -> 1
            '*', '/' -> 2
            else -> 0
        }
    }

    private fun applyOperation(numbers: MutableList<Double>, operators: MutableList<Char>) {
        if (numbers.size < 2 || operators.isEmpty()) {
            throw IllegalArgumentException("Недостаточно операндов для операции")
        }

        val b = numbers.removeAt(numbers.size - 1)
        val a = numbers.removeAt(numbers.size - 1)
        val op = operators.removeAt(operators.size - 1)

        val result = when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> {
                if (abs(b) < 1e-10) {
                    throw IllegalArgumentException("Деление на ноль")
                }
                a / b
            }
            else -> throw IllegalArgumentException("Неизвестная операция: '$op'")
        }

        numbers.add(result)
    }

    private fun formatResult(value: Double): String {
        if (value.isInfinite() || value.isNaN()) {
            throw IllegalArgumentException("Результат не является числом")
        }

        if (abs(value - value.toLong().toDouble()) < 1e-10) {
            return value.toLong().toString()
        }

        val df = java.text.DecimalFormat("#.##########")
        df.decimalFormatSymbols = java.text.DecimalFormatSymbols(java.util.Locale.US)
        var result = df.format(value)

        result = result.trimEnd('0')
        result = result.trimEnd('.')

        return if (result.isEmpty() || result == "-") "0" else result
    }
}