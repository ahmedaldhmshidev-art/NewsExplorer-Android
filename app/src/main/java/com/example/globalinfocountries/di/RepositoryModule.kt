package com.example.globalinfocountries.di

import com.example.globalinfocountries.data.repository.CountryRepositoryImp
import com.example.globalinfocountries.data.repository.CountryRepositoryInterface
import com.example.globalinfocountries.data.repository.NewsRepositoryImp
import com.example.globalinfocountries.data.repository.NewsRepositoryInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class CountryRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCountryRepository(
        impl: CountryRepositoryImp
    ): CountryRepositoryInterface
}




//
//@Module
//@InstallIn(SingletonComponent::class)
//object NewsRepositoryModule {
//
//
//    @Provides
//    @Singleton
//    fun providesRepositoryNews(api: ApiNews): NewsRepositoryImp {
//        return NewsRepositoryImp(api)
//    }

@Module
@InstallIn(SingletonComponent::class)
abstract class NewsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        impl: NewsRepositoryImp
    ): NewsRepositoryInterface
}


