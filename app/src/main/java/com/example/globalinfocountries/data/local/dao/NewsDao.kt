package com.example.globalinfocountries.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.globalinfocountries.data.local.entities.NewsEntity


@Dao
interface NewsDao{
    @Query("SELECT * FROM NewsEntity WHERE countryCodeEntity =:code")
    suspend fun getNewsByCodeFromRoom(code:String):List<NewsEntity>

    @Transaction
    suspend fun replaceByCountry(code: String,item:List<NewsEntity>){
        deleteNewsByCountry(code)
        insertNewsByCodeToRoom(item)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsByCodeToRoom(news :List<NewsEntity>)

    @Query("DELETE FROM NewsEntity WHERE countryCodeEntity =:code")
    suspend fun deleteNewsByCountry(code:String)
}