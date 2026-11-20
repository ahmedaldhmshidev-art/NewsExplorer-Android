package com.example.globalinfocountries.presentation.news

import com.example.globalinfocountries.domain.model.News

data class UiStatesNews (
    val isLoadingNews: Boolean = false,
    val listNews :List<News> = emptyList()
)