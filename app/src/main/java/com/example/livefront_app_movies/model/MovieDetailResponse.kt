package com.example.livefront_app_movies.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailResponse(
    @Json(name = "backdrop_path") val backdropPath: String? = null,
    @Json(name = "belongs_to_collection") val belongsToCollection: BelongsToCollection? = null,
    val budget: Int? = null,
    val genres: List<Genre> = emptyList(),
    val homepage: String? = null,
    val id: Int? = null,
    @Json(name = "imdb_id") val imdbId: String? = null,
    @Json(name = "origin_country") val originCountries: List<String> = emptyList(),
    @Json(name = "original_language") val originalLanguage: String? = null,
    @Json(name = "original_title") val originalTitle: String? = null,
    val overview: String? = null,
    @Json(name = "poster_path") val posterPath: String? = null,
    @Json(name = "production_companies") val productionCompanies: List<ProductionCompany> = emptyList(),
    @Json(name = "production_countries") val productionCountries: List<ProductionCountry> = emptyList(),
    @Json(name = "release_date") val releaseDate: String? = null,
    @Json(name = "spoken_languages") val spokenLanguages: List<SpokenLanguage> = emptyList(),
    val title: String? = null,
    val status: String? = null,
)

@JsonClass(generateAdapter = true)
data class Genre(
    val id: Int? = null,
    val name: String? = null
)

@JsonClass(generateAdapter = true)
data class BelongsToCollection(
    val id: Int? = null,
    val name: String? = null,
    @Json(name = "poster_path") val posterPath: String? = null,
)

@JsonClass(generateAdapter = true)
data class ProductionCompany(
    val id: Int? = null,
    @Json(name = "logo_path") val logoPath: String? = null,
    val name: String? = null,
    @Json(name = "origin_country") val originCountry: String? = null
)

@JsonClass(generateAdapter = true)
data class ProductionCountry(
    @Json(name = "iso_3166_1") val iso31661: String? = null,
    val name: String? = null
)

@JsonClass(generateAdapter = true)
data class SpokenLanguage(
    @Json(name = "english_name") val englishName: String? = null,
    @Json(name = "iso_639_1") val iso6391: String? = null,
    val name: String? = null
)