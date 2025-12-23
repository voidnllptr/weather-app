package com.example.weatherapp.feature.list.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.weatherapp.R
import com.example.weatherapp.feature.list.domain.model.Jewelry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class JewelryListViewModel : ViewModel() {

    private val _jewelryList = MutableStateFlow<List<Jewelry>>(emptyList())
    val jewelryList: StateFlow<List<Jewelry>> = _jewelryList.asStateFlow()

    init {
        _jewelryList.value = getMockJewelry()
    }

    private fun getMockJewelry(): List<Jewelry> = listOf(
        Jewelry(
            type = "keychain",
            brand = "Aliexpress",
            description = "Милый брелок с ангелочком",
            cost = 500,
            imageResId = R.drawable.jewelry_keychain_angel
        ),
        Jewelry(
            type = "keychain",
            brand = "Aliexpress",
            description = "Брелок с зубиком и сердцем",
            cost = 550,
            imageResId = R.drawable.jewelry_keychain_tooth_heart
        ),
        Jewelry(
            type = "hairclip",
            brand = "Sanrio",
            description = "Черная заколка с котиком",
            cost = 250,
            imageResId = R.drawable.jewelry_hairclip_black_cat
        ),
        Jewelry(
            type = "hairclip",
            brand = "Sanrio",
            description = "Заколка с белым мишкой",
            cost = 300,
            imageResId = R.drawable.jewelry_hairclip_white_bear
        ),
        Jewelry(
            type = "necklace",
            brand = "Aliexpress",
            description = "Серебряное ожерелье",
            cost = 1200,
            imageResId = R.drawable.jewelry_necklace_silver
        ),
        Jewelry(
            type = "necklace",
            brand = "Aliexpress",
            description = "Ожерелье с белым бантом",
            cost = 700,
            imageResId = R.drawable.jewelry_necklace_white_bow
        )
    )
}
