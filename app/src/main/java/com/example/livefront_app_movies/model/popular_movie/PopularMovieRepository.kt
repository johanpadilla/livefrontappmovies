package com.example.livefront_app_movies.model.popular_movie

import com.example.livefront_app_movies.network.NetworkResponse

interface PopularMovieRepository {

    suspend fun getPopularMovies(page: String): NetworkResponse<PopularMovieResponse, Throwable>
}