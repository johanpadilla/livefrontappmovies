package com.example.livefront_app_movies.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livefront_app_movies.model.movie_detail.MovieDetailRepository
import com.example.livefront_app_movies.model.movie_detail.MovieDetailResponse
import com.example.livefront_app_movies.network.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieDetailRepository,
    private val ioDispatcher: CoroutineContext
) : ViewModel() {
    private val _details = MutableStateFlow<MovieDetailState>(MovieDetailState.Loading)
    val detail: StateFlow<MovieDetailState> = _details.asStateFlow()

    /**
     * Fetch the movie detail from the APi if the movieId is not null or empty.
     * Use of flow, collect detail in your UI to get the data.
     * @param movieId - movieId to be fetch.
     * @return Unit
     */
    fun getMovieDetail(movieId: String?) {
        if (movieId != null && movieId.isEmpty().not()) {
            viewModelScope.launch(ioDispatcher) {
                val response = repository.getMovieDetail(movieId)
                _details.value = getStateFromResponse(response)
            }
        } else _details.value = MovieDetailState.Error
    }

    fun onRefresh(movieId: String?, onError: Boolean = false) {
        if(onError) {
            _details.value = MovieDetailState.Loading
        } else {
            _details.value = (_details.value as MovieDetailState.Loaded).copy(isRefreshing = true)
        }
        getMovieDetail(movieId)
    }

    private fun getStateFromResponse(movieDetailResponse: NetworkResponse<MovieDetailResponse, Throwable>): MovieDetailState {
        return when (movieDetailResponse) {
            is NetworkResponse.Success -> {
                val response = movieDetailResponse.body
                if (response.title.isNullOrEmpty().not()) {
                    MovieDetailState.Loaded(
                        movieDetail = response.toMovieDetail(), isRefreshing = false
                    )
                } else MovieDetailState.Empty

            }

            else -> MovieDetailState.Error
        }
    }
}