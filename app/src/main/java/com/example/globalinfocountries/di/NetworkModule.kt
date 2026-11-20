package com.example.globalinfocountries.di

import com.example.globalinfocountries.data.remote.api.ApiServiceCountry
import com.example.globalinfocountries.data.remote.api.ApiNews
import com.example.globalinfocountries.di.qualifier.CountriesApiQualifier
import com.example.globalinfocountries.di.qualifier.NewsApiQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @CountriesApiQualifier
    @Provides
    @Singleton
    fun provideCountriesRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://restcountries.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @CountriesApiQualifier
    @Provides
    @Singleton
    fun provideCountriesApi(
        @CountriesApiQualifier retrofit: Retrofit
    ): ApiServiceCountry =
        retrofit.create(ApiServiceCountry::class.java)


    @NewsApiQualifier
    @Provides
    @Singleton
    fun provideNewsRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.currentsapi.services/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @NewsApiQualifier
    @Provides
    @Singleton
    fun provideNewsApi(
        @NewsApiQualifier retrofit: Retrofit
    ): ApiNews =
        retrofit.create(ApiNews::class.java)
}



//
//@Module
//    @InstallIn(SingletonComponent::class)
//object NetworkModule{
//
//    @CountriesApiQualifier
//    @Provides
//    @Singleton
//    fun providesCountryRetrofit(): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl("https://restcountries.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun providesCountryApi(@CountriesApiQualifier retrofit: Retrofit):ApiServiceCountry{
//        return retrofit.create(ApiServiceCountry::class.java)
//    }
//
//
//    @Provides
//    @Singleton
//    @NewsApiQualifier
//    fun providesNewsRetrofit():Retrofit{
//        return Retrofit.Builder()
//            .baseUrl("https://newsapi.org/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun providesNewsApi(@NewsApiQualifier retrofit: Retrofit):ApiNews{
//        return retrofit.create(ApiNews::class.java)
//    }
//}
