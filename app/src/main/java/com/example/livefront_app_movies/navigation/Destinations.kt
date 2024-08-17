package com.example.livefront_app_movies.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Destinations(val route: String) {
    @Serializable
    data object PopularMovie: Destinations(route = "popular_movie")
    @Serializable
    data object MovieDetail: Destinations(route = "movie_detail/{id}")
}