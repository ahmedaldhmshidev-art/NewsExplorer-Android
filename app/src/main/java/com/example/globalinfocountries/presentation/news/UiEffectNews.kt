package com.example.globalinfocountries.presentation.news

import com.example.globalinfocountries.utils.Exception.UiError

sealed class UiEffectNews {
    data class OnOpenLink(val uri:String): UiEffectNews()
    data class ShowErrorNews(val uiError: UiError):UiEffectNews()
}
