package com.example.livefront_app_movies.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livefront_app_movies.di.IoDispatcher
import com.example.livefront_app_movies.model.popular_movie.PopularMovieRepository
import com.example.livefront_app_movies.model.popular_movie.PopularMovieResponse
import com.example.livefront_app_movies.network.movie.MovieService
import com.example.livefront_app_movies.network.NetworkResponse
import com.example.livefront_app_movies.ui.flow.makeRestartable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class PopularMovieViewModel @Inject constructor(
    private val repository: PopularMovieRepository,
   private val ioDispatcher: CoroutineContext,
) : ViewModel() {

    private val restarter = SharingStarted.WhileSubscribed(FIVE_SECONDS).makeRestartable()

    val movies: StateFlow<PopularMovieState> by lazy {
        flow {
            emit(getStateFromResponse(repository.getPopularMovies(
                mapOf(
                    "language" to "en-US",
                    "page" to "1"
                )
            )))
        }.catch { emit(PopularMovieState.Empty) }
            .flowOn(ioDispatcher)
            .stateIn(
                scope = viewModelScope,
                started = restarter,
                initialValue = PopularMovieState.Loading
            )
    }


    private fun getStateFromResponse(moviesResponse: NetworkResponse<PopularMovieResponse, Throwable>): PopularMovieState {
        return when (moviesResponse) {
            is NetworkResponse.Success -> {
                val body = moviesResponse.body
                if (body.results.isNotEmpty()) {
                    PopularMovieState.Loaded(
                        currentPage = moviesResponse.body.page,
                        totalPages = moviesResponse.body.totalPages,
                        movies = moviesResponse.body.results.map { it.toPopularMovie() })
                } else PopularMovieState.Empty
            }

            else -> PopularMovieState.Error
        }
    }

    fun restart() = restarter.restart()

    companion object {
        private const val FIVE_SECONDS: Long = 5_000
    }
}