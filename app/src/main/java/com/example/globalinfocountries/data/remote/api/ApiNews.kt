package com.example.globalinfocountries.data.remote.api

import com.example.globalinfocountries.data.remote.models.NewsResponseDto
import com.example.globalinfocountries.utils.ApiKeys
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiNews {

    @GET("v1/latest-news")
    suspend fun getNewsByCountry(
        @Query("country") country: String,

        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String = ApiKeys.NEWS_API_KEY
    ): NewsResponseDto
}