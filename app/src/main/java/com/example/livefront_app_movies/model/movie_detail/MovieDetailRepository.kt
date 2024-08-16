package com.example.livefront_app_movies.model.movie_detail

import com.example.livefront_app_movies.network.NetworkResponse

interface MovieDetailRepository {
    suspend fun getMovieDetail(movieId: String) : NetworkResponse<MovieDetailResponse, Throwable>
}