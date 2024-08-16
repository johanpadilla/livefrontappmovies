package com.example.livefront_app_movies.di

import com.example.livefront_app_movies.model.movie_detail.MovieDetailRepository
import com.example.livefront_app_movies.model.movie_detail.MovieDetailRepositoryImpl
import com.example.livefront_app_movies.model.popular_movie.PopularMovieRepository
import com.example.livefront_app_movies.model.popular_movie.PopularMovieRepositoryImpl
import com.example.livefront_app_movies.ui.details.MovieDetailViewModel
import com.example.livefront_app_movies.ui.home.PopularMovieViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindMovieDetailRepository(impl: MovieDetailRepositoryImpl): MovieDetailRepository

    @Binds
    abstract fun bindPopularMovieRepository(impl: PopularMovieRepositoryImpl): PopularMovieRepository


}

@Module
@InstallIn(ViewModelComponent::class)
object ViewModel {

    @Provides
    fun providePopularMovieViewModel(
        repository: PopularMovieRepository,
        @IoDispatcher ioDispatcher: CoroutineContext
    ) = PopularMovieViewModel(repository, ioDispatcher)

    @Provides
    fun provideDetailMovieViewModel(
        repository: MovieDetailRepository,
        @IoDispatcher ioDispatcher: CoroutineContext
    ) = MovieDetailViewModel(repository, ioDispatcher)

}
