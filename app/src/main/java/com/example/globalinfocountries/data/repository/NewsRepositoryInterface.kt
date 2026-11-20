package com.example.globalinfocountries.data.repository

import com.example.globalinfocountries.domain.model.News
import com.example.globalinfocountries.utils.DataResult
import kotlinx.coroutines.flow.Flow

interface NewsRepositoryInterface {
    fun getNewsByCountry(
        countryCode: String
    ): Flow<DataResult<List<News>>>
}