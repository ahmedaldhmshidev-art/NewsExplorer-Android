package com.example.globalinfocountries.data.repository

import com.example.globalinfocountries.data.local.dao.NewsDao
import com.example.globalinfocountries.data.mapper.entityToNewsModel
import com.example.globalinfocountries.data.mapper.toNews
import com.example.globalinfocountries.data.mapper.toNewsEntity
import com.example.globalinfocountries.data.remote.api.ApiNews
import com.example.globalinfocountries.di.qualifier.NewsApiQualifier
import com.example.globalinfocountries.domain.model.News
import com.example.globalinfocountries.utils.DataResult
import com.example.globalinfocountries.utils.Exception.ErrorMapper
import com.example.globalinfocountries.utils.Exception.UiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NewsRepositoryImp @Inject constructor(
    @NewsApiQualifier private val api : ApiNews,
    private val dao: NewsDao ,
    private val errorMapper :ErrorMapper
): NewsRepositoryInterface
{
    override fun getNewsByCountry(countryCode: String): Flow<DataResult<List<News>>> = flow{
       val local = dao.getNewsByCodeFromRoom(countryCode) // newsEntity
        if (local.isNullOrEmpty())
            {
            emit(DataResult.Error(UiError.EmptyData))
            }
        else {
            val mapperToModel = local.map { it.entityToNewsModel() }
            emit(DataResult.Success(mapperToModel))
        }
        try {
            val remote = api.getNewsByCountry(countryCode) // newsDto
            if (remote.news.isNullOrEmpty()) {
                emit(DataResult.Error(UiError.EmptyData))
            }
            else {
                val mapperToModel = remote.news.map { it.toNews() }
                val mapperToEntity = mapperToModel.map { it.toNewsEntity(countryCode) }
                dao.replaceByCountry(countryCode, mapperToEntity)
                val mapperToModel2 = mapperToEntity.map { it.entityToNewsModel() }
                emit(DataResult.Success(mapperToModel2))
            }
        }catch (e:Exception){
            emit(DataResult.Error(errorMapper.map(e)))
        }
    }
}