package com.example.livefront_app_movies.network.movie

import com.example.livefront_app_movies.model.movie_detail.MovieDetailResponse
import com.example.livefront_app_movies.model.popular_movie.PopularMovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MovieService {

    @GET("3/movie/popular?language=en-US")
    suspend fun getPopularMovies(@Query("page") page: String): PopularMovieResponse

    @GET("3/movie/{movieId}")
    suspend fun getMovieDetail(@Path("movieId") movieId: String): MovieDetailResponse
}