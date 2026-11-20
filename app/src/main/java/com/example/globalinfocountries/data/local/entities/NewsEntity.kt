package com.example.globalinfocountries.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsEntity(
 @PrimaryKey(autoGenerate = true) val id : Int = 0,
 val titleEntity : String,
 val descriptionEntity:String,
 val imageUriEntity : String,
 val sourceEntity :String,
 val linkEntity :String,
 val authorEntity :String="",
 val publishedEntity:String,
 val countryCodeEntity:String
)
