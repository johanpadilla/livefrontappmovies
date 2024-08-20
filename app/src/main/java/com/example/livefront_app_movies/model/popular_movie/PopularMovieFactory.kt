package com.example.livefront_app_movies.model.popular_movie


fun createPopularMovieResponse() = PopularMovieResponse(
    page = 1,
    results = listOf(createResult()),
    totalPages = 500,
    totalResults = 6300
)

private fun createResult() = Results(
    id = 123,
    originalLanguage = "en-US",
    originalTitle = "Mock Original Title",
    title = "Mock Title",
    releaseDate = "2024-07-09"
)