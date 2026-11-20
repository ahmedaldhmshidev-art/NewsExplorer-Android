package com.example.globalinfocountries.utils.Exception

import androidx.annotation.StringRes
import com.example.globalinfocountries.R

sealed class UiError(@StringRes val messageRes :Int , val canRetry:Boolean) {
    object NoInternet: UiError(R.string.error_no_internet , true)
    object Timeout : UiError(R.string.error_timeout , false )
    object Server: UiError(R.string.error_server , true)
    object NotFound: UiError(R.string.error_not_found , false)
    object EmptyData: UiError(R.string.error_empty_data , false)
    object Unknown: UiError(R.string.error_unknown,false)
    object TooManyRequests : UiError(R.string.error_tooManyRequests ,false)
    object NoFoundNewsCountry : UiError(R.string.error_noFoundNewsCountry , false)

}