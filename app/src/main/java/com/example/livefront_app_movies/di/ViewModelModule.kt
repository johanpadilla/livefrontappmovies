package com.example.livefront_app_movies.di

import com.example.livefront_app_movies.network.movie.MovieService
import com.example.livefront_app_movies.ui.details.MovieDetailViewModel
import com.example.livefront_app_movies.ui.home.PopularMovieViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    fun provideViewModel(
        movieService: MovieService,
        @IoDispatcher dispatcher: CoroutineContext,
    ) =
        PopularMovieViewModel(movieService, dispatcher)

    @Provides
    fun provideDetailViewModel(
        movieService: MovieService,
        @IoDispatcher dispatcher: CoroutineContext,
    ) = MovieDetailViewModel(movieService, dispatcher)
}