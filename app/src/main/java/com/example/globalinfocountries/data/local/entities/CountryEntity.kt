package com.example.globalinfocountries.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CountryEntity(
    @PrimaryKey(autoGenerate = true) val idEntity :Int = 0,
    val nameEntity :String,
    val codeEntity :String,
    val capitalEntity :String,
    val populationEntity : Long,
    val flagEntity :String
)
