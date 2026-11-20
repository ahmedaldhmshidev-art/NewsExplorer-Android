package com.example.globalinfocountries.data.mapper

import com.example.globalinfocountries.data.local.entities.CountryEntity
import com.example.globalinfocountries.data.local.entities.NewsEntity
import com.example.globalinfocountries.domain.model.Country
import com.example.globalinfocountries.domain.model.News

fun Country.toCountryEntity():CountryEntity{
    return CountryEntity(
        nameEntity = this.name ,
        codeEntity = this.code ,
        capitalEntity = this.capital ,
        populationEntity = this.population,
        flagEntity = this.flagUrl
    )
}

fun News.toNewsEntity(countryCode:String):NewsEntity{
    return NewsEntity(
        titleEntity = this.title,
        descriptionEntity = this.description,
        sourceEntity = this.source,
        publishedEntity = this.publishedAt,
        imageUriEntity = this.imageUrl,
        linkEntity = this.url,
        countryCodeEntity = countryCode,
        authorEntity = this.author
    )
}