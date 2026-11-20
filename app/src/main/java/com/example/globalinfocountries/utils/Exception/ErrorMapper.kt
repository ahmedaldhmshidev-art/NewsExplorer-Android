package com.example.globalinfocountries.utils.Exception

import javax.inject.Inject

class ErrorMapper @Inject constructor(){
    fun map(throwable: Throwable): UiError {
    return when(throwable){
        is java.io.IOException ->{
            UiError.NoInternet
        }
        is retrofit2.HttpException->{
            when(throwable.code()){
                404 -> UiError.NotFound
                400 -> UiError.NoFoundNewsCountry
                429 -> UiError.TooManyRequests
               in 500..599 -> UiError.Server
                else -> {
                    UiError.Unknown
                }
            }
        }
        is kotlinx.coroutines.TimeoutCancellationException ->{
            UiError.Timeout
        }
        else ->{
            UiError.Unknown
        }
    }    }


}