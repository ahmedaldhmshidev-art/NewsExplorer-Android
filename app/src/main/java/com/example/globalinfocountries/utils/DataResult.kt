package com.example.globalinfocountries.utils

import com.example.globalinfocountries.utils.Exception.UiError

sealed class DataResult<out T>{
    data class Success<T>(val data:T):DataResult<T>()
    data class Error(val error : UiError):DataResult<Nothing>()
}