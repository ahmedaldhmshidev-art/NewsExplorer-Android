package com.example.globalinfocountries.presentation.countries

import com.example.globalinfocountries.utils.Exception.UiError

sealed class UiEffectCountry {
    data class Navigation(val code : String): UiEffectCountry()
    data class ShowErrorCountry(val error: UiError):UiEffectCountry()
}