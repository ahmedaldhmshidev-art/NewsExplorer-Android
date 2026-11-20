package com.example.globalinfocountries.data.remote.api

import com.example.globalinfocountries.data.remote.models.CountryDto
import retrofit2.http.GET
interface ApiServiceCountry {

    @GET("v3.1/all?fields=name,cca2,capital,population,flags")
    suspend fun getAllCountry(): List<CountryDto>

}

