package com.example.livefront_app_movies.network.movie

import com.example.livefront_app_movies.model.movie_detail.MovieDetailResponse
import com.example.livefront_app_movies.model.popular_movie.PopularMovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: String = "1"
    ): PopularMovieResponse

    @GET("3/movie/{movieId}")
    suspend fun getMovieDetail(@Path("movieId") movieId: String): MovieDetailResponse
}