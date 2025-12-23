package com.example.weatherapp.feature.list.di
import com.example.weatherapp.feature.list.presentation.viewmodel.JewelryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listModule = module {

    viewModel {JewelryListViewModel() }

}