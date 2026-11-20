package com.example.globalinfocountries.di

import android.content.Context
import androidx.room.Room
import com.example.globalinfocountries.data.local.AppDatabase
import com.example.globalinfocountries.data.local.dao.CountryDao
import com.example.globalinfocountries.data.local.dao.NewsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

    @Module
    @InstallIn(SingletonComponent::class)
    object DatabaseModule {

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context, AppDatabase::class.java, "CountryNewsDB"
            ).build()
        }


            @Provides
            fun provideCountryDao(db: AppDatabase): CountryDao = db.countryDao()

            @Provides
            fun providesNewsDao(database :AppDatabase ):NewsDao =database.newsDao()


    }
