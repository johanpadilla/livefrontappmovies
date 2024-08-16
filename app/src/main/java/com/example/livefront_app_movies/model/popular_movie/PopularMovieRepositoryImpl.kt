package com.example.livefront_app_movies.model.popular_movie

import com.example.livefront_app_movies.network.NetworkResponse
import com.example.livefront_app_movies.network.movie.MovieService
import javax.inject.Inject

class PopularMovieRepositoryImpl @Inject constructor(private val remoteDataSource: MovieService) :
    PopularMovieRepository {

    override suspend fun getPopularMovies(queryParam: Map<String, String>): NetworkResponse<PopularMovieResponse, Throwable> {
        return try {
            NetworkResponse.Success(remoteDataSource.getPopularMovies(queryParam))
        }
        catch (ex: Exception) {
            NetworkResponse.NetworkError(ex.cause)
        }
    }

}