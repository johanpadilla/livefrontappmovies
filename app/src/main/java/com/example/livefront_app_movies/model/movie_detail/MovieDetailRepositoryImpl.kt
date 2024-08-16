package com.example.livefront_app_movies.model.movie_detail

import com.example.livefront_app_movies.network.NetworkResponse
import com.example.livefront_app_movies.network.movie.MovieService
import javax.inject.Inject

class MovieDetailRepositoryImpl @Inject constructor(
    private val movieService: MovieService
) : MovieDetailRepository {

    override suspend fun getMovieDetail(movieId: String): NetworkResponse<MovieDetailResponse, Throwable> {
        return try {
            NetworkResponse.Success( movieService.getMovieDetail(movieId))
        } catch (ex: Exception) {
            NetworkResponse.NetworkError(ex.cause)
        }
    }
}