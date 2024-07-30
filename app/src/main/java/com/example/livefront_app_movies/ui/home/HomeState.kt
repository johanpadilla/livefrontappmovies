package com.example.livefront_app_movies.ui.home

import com.example.livefront_app_movies.model.Results
import com.example.livefront_app_movies.utils.toFullPosterURL

sealed class HomeState {
    data object Loading : HomeState()
    data class Loaded(
        val currentPage: Int?,
        val totalPages: Int?,
        val movies: List<PopularMovie>
    ) : HomeState()

    data object Empty : HomeState()
    data object Error : HomeState()
}

data class PopularMovie(
    val id: Int?,
    val originalLanguage: String? = null,
    val originalTitle: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val posterPath: String? = null,
    val releaseDate: String? = null,
    val title: String? = null,
    val voteCount: Int? = null,
    val fullPosterUrl: String? = null
)

fun Results.toPopularMovie() = PopularMovie(
    id = this.id,
    originalLanguage = this.originalLanguage,
    originalTitle = this.originalTitle,
    overview = this.overview,
    popularity = this.popularity,
    posterPath = this.posterPath,
    releaseDate = this.releaseDate,
    title = this.title,
    voteCount = this.voteCount,
    fullPosterUrl = this.posterPath?.toFullPosterURL()
)