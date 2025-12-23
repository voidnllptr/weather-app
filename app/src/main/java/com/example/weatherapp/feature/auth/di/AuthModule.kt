package com.example.weatherapp.feature.auth.di

import com.example.weatherapp.core.local.AppDatabase
import com.example.weatherapp.feature.auth.data.dao.UserDao
import com.example.weatherapp.feature.auth.domain.repository.AuthRepository
import com.example.weatherapp.feature.auth.data.repository.AuthRepositoryImpl
import com.example.weatherapp.feature.auth.domain.validation.Validator
import com.example.weatherapp.feature.auth.presentation.viewmodel.AuthViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single {
        AppDatabase.getInstance(androidContext())
    }

    single<UserDao> { get<AppDatabase>().userDao() }

    single<AuthRepository> {
        AuthRepositoryImpl(get())
    }

    single {
        Validator(androidContext())
    }

    viewModel {
        AuthViewModel(
            authRepository = get(),
            validator = get()
        )
    }
}