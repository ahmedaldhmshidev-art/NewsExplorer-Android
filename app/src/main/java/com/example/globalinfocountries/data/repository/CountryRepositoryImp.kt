package com.example.globalinfocountries.data.repository

import com.example.globalinfocountries.data.local.dao.CountryDao
import com.example.globalinfocountries.data.mapper.entityToCountryModel
import com.example.globalinfocountries.data.remote.api.ApiServiceCountry
import com.example.globalinfocountries.data.mapper.toCountry
import com.example.globalinfocountries.data.mapper.toCountryEntity
import com.example.globalinfocountries.di.qualifier.CountriesApiQualifier
import com.example.globalinfocountries.domain.model.Country
import com.example.globalinfocountries.utils.DataResult
import com.example.globalinfocountries.utils.Exception.ErrorMapper
import com.example.globalinfocountries.utils.Exception.UiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CountryRepositoryImp @Inject constructor(
    @CountriesApiQualifier private val apiCountry: ApiServiceCountry,
    private val dao: CountryDao ,
    private val errorMapper : ErrorMapper ,

):
    CountryRepositoryInterface {
    override fun getCountries(): Flow<DataResult<List<Country>>> = flow {
        val local = dao.getAllCountryFromRoom() // entity
        if (local.isNullOrEmpty())
        {
            emit(DataResult.Error(UiError.EmptyData))
        }
        else
        {
            val mapper = local.map { it.entityToCountryModel() }
            emit(DataResult.Success(mapper))
        }
        try {
            val remote = apiCountry.getAllCountry() // dto
            if (remote.isNullOrEmpty())
            {
                emit(DataResult.Error(UiError.EmptyData))
            }
            else
            {
                val mapperToMode = remote.map { it.toCountry() }
                val mapperToEntity = mapperToMode.map { it.toCountryEntity() }
                dao.replace(mapperToEntity)
                val mapperToMode2 = mapperToEntity.map { it.entityToCountryModel() }
                emit(DataResult.Success(mapperToMode2))
            }
        }catch (e:Exception){
            emit(DataResult.Error(errorMapper.map(e)))
        }

    }

    override fun searchCountry(name: String): Flow<DataResult<List<Country>>> = flow{
        try {
            val search =  dao.searchCountry("%$name%")
            val mapper = search.map { it.entityToCountryModel() }
            emit(DataResult.Success(mapper))
        }catch (e:Exception)  {
            emit(DataResult.Error(errorMapper.map(e)))
        }
        }



}

