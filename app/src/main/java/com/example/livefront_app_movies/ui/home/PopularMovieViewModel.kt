package com.example.livefront_app_movies.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livefront_app_movies.model.popular_movie.PopularMovieRepository
import com.example.livefront_app_movies.model.popular_movie.PopularMovieResponse
import com.example.livefront_app_movies.network.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMovieViewModel @Inject constructor(
    private val repository: PopularMovieRepository,
) : ViewModel() {
    private val _popularMovies = MutableStateFlow<PopularMovieState>(PopularMovieState.Loading)
    val popularMovies: StateFlow<PopularMovieState> = _popularMovies.asStateFlow()

    /**
     * Fetch the popular movies from the API.
     */
    fun getPopularMovies() {
        viewModelScope.launch {
            val response = repository.getPopularMovies()
            _popularMovies.value = getStateFromResponse(response)
        }
    }

    /**
     * Function to trigger the reload of the content.
     * @param onError  (true) - if the refresh in on an error state it will change the PopularMovieState to Loading
     * @param onError  (false) - the refresh is NOT an error state, it will just update the field isRefreshing from the Loaded State
     */
    fun refresh(onError: Boolean = false) {
        if(onError) {
            _popularMovies.value = PopularMovieState.Loading
        } else {
            _popularMovies.value =
                (_popularMovies.value as PopularMovieState.Loaded).copy(isRefreshing = true)
        }
        getPopularMovies()
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
}