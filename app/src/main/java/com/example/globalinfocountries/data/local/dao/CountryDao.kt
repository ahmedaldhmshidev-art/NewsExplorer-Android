package com.example.globalinfocountries.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.globalinfocountries.data.local.entities.CountryEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CountryDao {
    @Query("SELECT * FROM CountryEntity")
    suspend fun getAllCountryFromRoom():List<CountryEntity>

    @Transaction
    suspend fun replace(item :List<CountryEntity>){
        clearAllCountriesFromRoom()
        insertAllCountryToRoom(item)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCountryToRoom(countries :List<CountryEntity>)

    @Query("DELETE FROM CountryEntity")
    suspend fun clearAllCountriesFromRoom()

    @Query("SELECT * FROM CountryEntity WHERE nameEntity LIKE :name")
   suspend fun searchCountry(name :String):List<CountryEntity>
}