package com.example.globalinfocountries.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.globalinfocountries.data.local.dao.CountryDao
import com.example.globalinfocountries.data.local.dao.NewsDao
import com.example.globalinfocountries.data.local.entities.CountryEntity
import com.example.globalinfocountries.data.local.entities.NewsEntity

    @Database(
        entities = [CountryEntity::class,
                   NewsEntity::class],
        version = 2,
        exportSchema = false
    )
    abstract class AppDatabase : RoomDatabase() {

        abstract fun countryDao(): CountryDao
        abstract fun newsDao(): NewsDao

        companion object {

            @Volatile
            private var INSTANCE: AppDatabase? = null

            fun getDatabase(context: Context): AppDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "product_database"
                    )
                        .fallbackToDestructiveMigration() // ⬅️ الحذف وإعادة البناء
                        .build()

                    INSTANCE = instance
                    instance
                }
            }
        }
    }



