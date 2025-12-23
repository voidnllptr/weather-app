package com.example.weatherapp.feature.calculator.di

import com.example.weatherapp.feature.calculator.data.repository.CalculatorRepositoryImpl
import com.example.weatherapp.feature.calculator.domain.repository.CalculatorRepository
import com.example.weatherapp.feature.calculator.domain.usecase.CalculateExpressionUseCase
import com.example.weatherapp.feature.calculator.presentation.viewmodel.CalculatorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val calculatorModule = module {
    single<CalculatorRepository> { CalculatorRepositoryImpl() }

    factory { CalculateExpressionUseCase(get()) }

    viewModel { CalculatorViewModel(get()) }
}