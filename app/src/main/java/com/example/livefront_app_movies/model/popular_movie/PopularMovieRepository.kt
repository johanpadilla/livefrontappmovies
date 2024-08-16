package com.example.livefront_app_movies.model.popular_movie

import com.example.livefront_app_movies.network.NetworkResponse

interface PopularMovieRepository {

    suspend fun getPopularMovies(queryParam: Map<String, String>): NetworkResponse<PopularMovieResponse, Throwable>
}