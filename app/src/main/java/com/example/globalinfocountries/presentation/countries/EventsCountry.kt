package com.example.globalinfocountries.presentation.countries

import com.example.globalinfocountries.utils.Exception.UiError

sealed class EventsCountry {
    data class OnCountryClicked(val countryCode:String): EventsCountry()
    data class OnSearchQueryChanged(val query: String) : EventsCountry()

    object Refresh : EventsCountry()
    object Load : EventsCountry()
}
