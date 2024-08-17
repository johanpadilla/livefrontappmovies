package com.example.livefront_app_movies.di

import com.example.livefront_app_movies.model.movie_detail.MovieDetailRepository
import com.example.livefront_app_movies.model.movie_detail.MovieDetailRepositoryImpl
import com.example.livefront_app_movies.model.popular_movie.PopularMovieRepository
import com.example.livefront_app_movies.model.popular_movie.PopularMovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindMovieDetailRepository(impl: MovieDetailRepositoryImpl): MovieDetailRepository

    @Binds
    abstract fun bindPopularMovieRepository(impl: PopularMovieRepositoryImpl): PopularMovieRepository

}


