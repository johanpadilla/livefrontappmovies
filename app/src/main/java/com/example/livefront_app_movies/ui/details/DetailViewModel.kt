package com.example.livefront_app_movies.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livefront_app_movies.model.MovieDetailResponse
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
class DetailViewModel @Inject constructor(private val movieService: MovieService) : ViewModel() {
    private val _details = MutableStateFlow<MovieDetailState>(MovieDetailState.Empty)
    val detail: StateFlow<MovieDetailState> = _details.asStateFlow()

    fun getMovieDetail(movieId: String?) {
        movieId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val response = performApiCall(Dispatchers.IO) {movieService.getMovieDetail(movieId)}
                _details.value = getStateFromResponse(response)
            }
        }
    }

    private fun getStateFromResponse(movieDetailResponse: NetworkResponse<MovieDetailResponse>): MovieDetailState {
        return when(movieDetailResponse) {
            is NetworkResponse.Success -> {
                val response = movieDetailResponse.body
                if(response.title.isNullOrEmpty().not()) {
                    MovieDetailState.Loaded(
                        movieDetail = response.toMovieDetail()
                    )
                } else MovieDetailState.Empty

            }
            else -> MovieDetailState.Error
        }
    }
}