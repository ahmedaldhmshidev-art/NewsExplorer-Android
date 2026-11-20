package com.example.globalinfocountries.presentation.news

sealed class EventNews {
    data class OnClickLink(val uri :String): EventNews()

    data class LoadNews(val code:String): EventNews()
    data class Refresh(val code: String): EventNews()

}

