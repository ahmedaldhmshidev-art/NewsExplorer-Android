package com.example.globalinfocountries.presentation.countries

import com.example.globalinfocountries.domain.model.Country

data class UiStatesCountry (
//    object Idle : UiStatesCountry()
//    object Loading : UiStatesCountry()
//    data class Success (val counties: List<Country>) : UiStatesCountry()

    val isLoading:Boolean = false ,
    val listCountry:List<Country> = emptyList(),
    val searchQuery: String = ""


)