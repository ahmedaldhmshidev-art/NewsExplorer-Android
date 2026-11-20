package com.example.globalinfocountries.data.repository

import com.example.globalinfocountries.domain.model.Country
import com.example.globalinfocountries.utils.DataResult
import kotlinx.coroutines.flow.Flow

interface CountryRepositoryInterface
{
    fun getCountries(): Flow<DataResult<List<Country>>>
    fun searchCountry(name:String):Flow<DataResult<List<Country>>>
}