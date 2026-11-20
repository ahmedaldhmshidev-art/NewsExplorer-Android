package com.example.globalinfocountries.data.mapper

import com.example.globalinfocountries.data.local.entities.CountryEntity
import com.example.globalinfocountries.data.local.entities.NewsEntity
import com.example.globalinfocountries.domain.model.Country
import com.example.globalinfocountries.domain.model.News


fun CountryEntity.entityToCountryModel():Country
{
    return Country(
        name = this.nameEntity,
        code = this.codeEntity,
        flagUrl = this.flagEntity,
        population = this.populationEntity,
        capital = this.capitalEntity
    )
}
fun NewsEntity.entityToNewsModel():News{
    return News(
        title = this.titleEntity,
        description = this.descriptionEntity,
        source = this.sourceEntity,
        publishedAt = this.publishedEntity,
        imageUrl = this.imageUriEntity,
        url = titleEntity,
        author = this.authorEntity
    )
}