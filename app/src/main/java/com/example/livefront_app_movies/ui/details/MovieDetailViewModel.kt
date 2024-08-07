package com.example.livefront_app_movies.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livefront_app_movies.di.IoDispatcher
import com.example.livefront_app_movies.model.MovieDetailResponse
import com.example.livefront_app_movies.network.movie.MovieService
import com.example.livefront_app_movies.network.NetworkResponse
import com.example.livefront_app_movies.network.utils.performApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val movieService: MovieService,  @IoDispatcher private val ioDispatcher: CoroutineContext) : ViewModel() {
    private val _details = MutableStateFlow<MovieDetailState>(MovieDetailState.Loading)
    val detail: StateFlow<MovieDetailState> = _details.asStateFlow()

    fun getMovieDetail(movieId: String?) {
        if(movieId != null && movieId.isEmpty().not()) {
            viewModelScope.launch(ioDispatcher) {
                _details.value = MovieDetailState.Loading
                val response = performApiCall(ioDispatcher) {movieService.getMovieDetail(movieId)}
                _details.value = getStateFromResponse(response)
            }
        } else _details.value = MovieDetailState.Error
    }

    private fun getStateFromResponse(movieDetailResponse: NetworkResponse<MovieDetailResponse>): MovieDetailState {
        return when(movieDetailResponse) {
            is NetworkResponse.Success -> {
                val response = movieDetailResponse.body
                if(response != null && response.title.isNullOrEmpty().not()) {
                    MovieDetailState.Loaded(
                        movieDetail = response.toMovieDetail()
                    )
                } else MovieDetailState.Empty

            }
            else -> MovieDetailState.Error
        }
    }
}