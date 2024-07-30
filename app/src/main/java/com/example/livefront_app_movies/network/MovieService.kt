package com.example.livefront_app_movies.network

import com.example.livefront_app_movies.model.MovieDetailResponse
import com.example.livefront_app_movies.model.PopularMovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface MovieService {

    @GET("3/movie/popular")
    suspend fun getPopularMovies(@QueryMap queryParam: Map<String, String>): PopularMovieResponse

    @GET("3/movie/{movieId}")
    suspend fun getMovieDetail(@Path("movieId") movieId: String): MovieDetailResponse
}