package com.example.livefront_app_movies.ui.details

import com.example.livefront_app_movies.model.movie_detail.MovieDetailResponse
import com.example.livefront_app_movies.utils.toFullPosterURL

sealed class MovieDetailState {
    data object Loading : MovieDetailState()
    data class Loaded(
        val movieDetail: MovieDetail? = null,
        val isRefreshing: Boolean = false
    ) : MovieDetailState()

    data object Empty : MovieDetailState()
    data object Error : MovieDetailState()
}

data class Genre(
    val id: Int? = null,
    val name: String? = null
)

data class BelongsToCollection(
    val id: Int? = null,
    val name: String? = null,
    val posterPath: String? = null,
)

data class ProductionCompany(
    val id: Int? = null,
    val logoPath: String? = null,
    val name: String? = null,
    val originCountry: String? = null
)

data class ProductionCountry(
    val name: String? = null
)

data class SpokenLanguage(
    val englishName: String? = null,
    val name: String? = null
)

data class MovieDetail(
    val budget: Int? = null,
    val belongsToCollection: BelongsToCollection? = null,
    val genres: List<Genre> = emptyList(),
    val homepage: String? = null,
    val id: Int? = null,
    val imdbId: String? = null,
    val originCountries: List<String> = emptyList(),
    val originalLanguage: String? = null,
    val originalTitle: String? = null,
    val overview: String? = null,
    val posterPath: String? = null,
    val productionCompanies: List<ProductionCompany> = emptyList(),
    val productionCountries: List<ProductionCountry> = emptyList(),
    val releaseDate: String? = null,
    val spokenLanguages: List<SpokenLanguage> = emptyList(),
    val title: String? = null,
    val status: String? = null,
    val fullPosterPath: String? = null
)

fun MovieDetailResponse.toMovieDetail() = MovieDetail(
    budget = this.budget,
    belongsToCollection = BelongsToCollection(
        this.belongsToCollection?.id,
        this.belongsToCollection?.name,
        this.belongsToCollection?.posterPath
    ),
    genres = this.genres.map { Genre(it.id, it.name) },
    homepage = this.homepage,
    id = this.id,
    imdbId = this.imdbId,
    originCountries = this.originCountries,
    originalLanguage = this.originalLanguage,
    originalTitle = this.originalTitle,
    overview = this.overview,
    posterPath = this.posterPath?.toFullPosterURL(),
    productionCompanies = this.productionCompanies.map {
        ProductionCompany(
            it.id,
            it.logoPath,
            it.name,
            it.originCountry
        )
    },
    productionCountries = this.productionCountries.map { ProductionCountry(it.name) },
    releaseDate = this.releaseDate,
    spokenLanguages = this.spokenLanguages.map { SpokenLanguage(it.englishName, it.name) },
    title = this.title,
    status = this.status,
    fullPosterPath = this.posterPath?.toFullPosterURL()
)
