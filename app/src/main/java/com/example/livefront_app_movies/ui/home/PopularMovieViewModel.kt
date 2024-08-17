package com.example.livefront_app_movies.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livefront_app_movies.model.popular_movie.PopularMovieRepository
import com.example.livefront_app_movies.model.popular_movie.PopularMovieResponse
import com.example.livefront_app_movies.network.NetworkResponse
import com.example.livefront_app_movies.ui.details.MovieDetailState
import com.example.livefront_app_movies.ui.flow.makeRestartable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class PopularMovieViewModel @Inject constructor(
    private val repository: PopularMovieRepository,
    private val ioDispatcher: CoroutineContext,
) : ViewModel() {
    private val _popularMovies = MutableStateFlow<PopularMovieState>(PopularMovieState.Loading)
    val popularMovies: StateFlow<PopularMovieState> = _popularMovies.asStateFlow()


    fun getPopularMovies(page: Int = 1) {
        viewModelScope.launch(ioDispatcher) {
            val response = repository.getPopularMovies(page.toString())
            _popularMovies.value = getStateFromResponse(response)
        }
    }


    private fun getStateFromResponse(moviesResponse: NetworkResponse<PopularMovieResponse, Throwable>): PopularMovieState {
        return when (moviesResponse) {
            is NetworkResponse.Success -> {
                val body = moviesResponse.body
                if (body.results.isNotEmpty()) {
                    PopularMovieState.Loaded(
                        currentPage = moviesResponse.body.page,
                        totalPages = moviesResponse.body.totalPages,
                        movies = moviesResponse.body.results.map { it.toPopularMovie() },
                        isRefreshing = false
                    )
                } else PopularMovieState.Empty
            }

            else -> PopularMovieState.Error
        }
    }

    fun refresh(onError: Boolean = false) {
        if(onError) {
            _popularMovies.value = PopularMovieState.Loading
        } else {
            _popularMovies.value =
                (_popularMovies.value as PopularMovieState.Loaded).copy(isRefreshing = true)
        }
        getPopularMovies()
    }
}