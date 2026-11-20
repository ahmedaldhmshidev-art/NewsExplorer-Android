package com.example.globalinfocountries.data.mapper

import com.example.globalinfocountries.data.remote.models.CountryDto
import com.example.globalinfocountries.data.remote.models.NewsDto
import com.example.globalinfocountries.domain.model.Country
import com.example.globalinfocountries.domain.model.News

fun CountryDto.toCountry():Country{
    return Country(
        name = this.name.common,
        code = this.cca2,
        capital = this.capital?.firstOrNull()?:"",
        flagUrl = this.flags.png,
        population = this.population
    )
}
fun NewsDto.toNews(): News {
    return News(
        title = title ?: "No title",
        description = description ?: "",
        imageUrl = image ?: "",
        source = source ?: "Unknown",
        publishedAt = published ?: "",
        url = url,
        author = author
    )
}






