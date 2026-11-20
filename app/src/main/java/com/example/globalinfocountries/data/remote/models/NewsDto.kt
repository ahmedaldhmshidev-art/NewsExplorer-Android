package com.example.globalinfocountries.data.remote.models

data class NewsResponseDto(
    val status:String? ,
    val news: List<NewsDto>
)

data class NewsDto(
    val title: String?,
    val description: String?,
    val url: String,
    val image: String?,
    val author: String,
    val category:List< String>?,
    val source: String?,
    val published: String?
)




