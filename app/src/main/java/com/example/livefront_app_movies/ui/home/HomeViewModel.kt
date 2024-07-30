package com.example.livefront_app_movies.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livefront_app_movies.model.PopularMovieResponse
import com.example.livefront_app_movies.network.MovieService
import com.example.livefront_app_movies.network.NetworkResponse
import com.example.livefront_app_movies.network.performApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val movieService: MovieService) : ViewModel() {
    private val _movies = MutableStateFlow<HomeState>(HomeState.Loading)
    val movies: StateFlow<HomeState> = _movies.asStateFlow()

    init {
        getMovies(1)
    }

    fun getMovies(page: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = performApiCall(Dispatchers.IO) {
                movieService.getPopularMovies(
                    mutableMapOf(
                        "language" to "en-US",
                        "page" to page.toString()
                    )
                )
            }
            _movies.value = getStateFromResponse(response)
        }
    }

    private fun getStateFromResponse(moviesResponse: NetworkResponse<PopularMovieResponse>): HomeState {
        return when (moviesResponse) {
            is NetworkResponse.Success -> {
                val body = moviesResponse.body
                if (body.results.isNotEmpty()) {
                    HomeState.Loaded(
                        currentPage = moviesResponse.body.page,
                        totalPages = moviesResponse.body.totalPages,
                        movies = moviesResponse.body.results.map { it.toPopularMovie() })
                } else HomeState.Empty
            }

            else -> HomeState.Error
        }
    }
}