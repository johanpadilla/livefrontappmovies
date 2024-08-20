package com.example.livefront_app_movies.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.livefront_app_movies.ui.details.MovieDetailViewModel
import com.example.livefront_app_movies.CoroutineRuleDispatcher
import com.example.livefront_app_movies.model.movie_detail.MovieDetailRepository
import com.example.livefront_app_movies.model.movie_detail.MovieDetailResponse
import com.example.livefront_app_movies.navigation.Argument
import com.example.livefront_app_movies.network.NetworkResponse
import com.example.livefront_app_movies.ui.details.MovieDetail
import com.example.livefront_app_movies.ui.details.MovieDetailState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MovieDetailsViewModelTest {

    private var repository: MovieDetailRepository = mock()
    private val savedStateHandle: SavedStateHandle = mock()

    private lateinit var viewModel: MovieDetailViewModel

    @get: Rule
    val coroutineRuleDispatcher = CoroutineRuleDispatcher()

    private val MOVIE_ID = "1234-ABC"

    @Test
    fun initialStateIsLoading() = runTest {
        whenever(savedStateHandle.get<String>(Argument.MOVIE_ID_KEY)).thenReturn(
            MOVIE_ID
        )

        viewModel = MovieDetailViewModel(repository, savedStateHandle)
        val shouldBeLoading = viewModel.detail.first()

        assert(shouldBeLoading is MovieDetailState.Loading)
    }

    @Test
    fun nullMOVIE_IDShouldBeErrorState() = runTest {
        whenever(savedStateHandle.get<String>(Argument.MOVIE_ID_KEY)).thenReturn(
            null
        )

        viewModel = MovieDetailViewModel(repository, savedStateHandle)
        viewModel.detail.test {
            val resultState = expectMostRecentItem()
            assert(resultState is MovieDetailState.Error)
        }
    }

    @Test
    fun withMOVIE_IDButNullBodyResponseShouldBeEmptyState() = runTest {
        whenever(savedStateHandle.get<String>(Argument.MOVIE_ID_KEY)).thenReturn(
            MOVIE_ID
        )

        viewModel = MovieDetailViewModel(repository, savedStateHandle)
        whenever(repository.getMovieDetail(MOVIE_ID)).thenReturn(
            NetworkResponse.Success(
                MovieDetailResponse()
            )
        )
        viewModel.detail.test {
            viewModel.getMovieDetail(MOVIE_ID)

            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is MovieDetailState.Loading)

            val shouldBeEmpty = awaitItem()
            assert(shouldBeEmpty is MovieDetailState.Empty)
        }
    }

    @Test
    fun withMOVIE_IDShouldBeLoadedState() = runTest {
        val title = "title"
        whenever(savedStateHandle.get<String>(Argument.MOVIE_ID_KEY)).thenReturn(
            MOVIE_ID
        )

        viewModel = MovieDetailViewModel(repository, savedStateHandle)
        whenever(repository.getMovieDetail(MOVIE_ID)).thenReturn(
            NetworkResponse.Success(
                MovieDetailResponse(
                    title = title
                )
            )
        )
        val movieDetail = MovieDetail(
            title = title
        )

        val loadedMovieDetail = MovieDetailState.Loaded(movieDetail = movieDetail)

        viewModel.detail.test {
            viewModel.getMovieDetail(MOVIE_ID)

            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is MovieDetailState.Loading)

            val shouldBeEmpty = awaitItem()
            assert(shouldBeEmpty is MovieDetailState.Loaded)
            assert(
                (shouldBeEmpty as MovieDetailState.Loaded).movieDetail!!.title.equals(
                    loadedMovieDetail.movieDetail?.title
                )
            )
        }
    }

    @Test
    fun withRestartNoResultsShouldBeEmptyState() = runTest {
        val popularMovieResponse = MovieDetailResponse()

        whenever(savedStateHandle.get<String>(Argument.MOVIE_ID_KEY)).thenReturn(
            MOVIE_ID
        )

        viewModel = MovieDetailViewModel(repository, savedStateHandle)
        whenever(repository.getMovieDetail(MOVIE_ID)).thenReturn(
            NetworkResponse.Success(popularMovieResponse)
        )

        viewModel.detail.test {
            viewModel.getMovieDetail(MOVIE_ID)

            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is MovieDetailState.Loading)
            val shouldBeEmpty = awaitItem()
            assert(shouldBeEmpty is MovieDetailState.Empty)

            viewModel.onRefresh(true)
            val shouldBeLoading2 = awaitItem()
            assert(shouldBeLoading2 is MovieDetailState.Loading)

            val shouldBeEmpty2 = awaitItem()
            assert(shouldBeEmpty2 is MovieDetailState.Empty)
        }
    }

    @Test
    fun withRestartResultsShouldBeLoadedState() = runTest {
        val title = "title"

        val popularMovieResponse = MovieDetailResponse(
            title = title
        )

        whenever(savedStateHandle.get<String>(Argument.MOVIE_ID_KEY)).thenReturn(
            MOVIE_ID
        )

        viewModel = MovieDetailViewModel(repository, savedStateHandle)
        whenever(repository.getMovieDetail(MOVIE_ID)).thenReturn(
            NetworkResponse.Success(popularMovieResponse)
        )

        viewModel.detail.test {
            viewModel.getMovieDetail(MOVIE_ID)

            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is MovieDetailState.Loading)
            val shouldBeLoadedNotRefreshing = awaitItem()
            assert((shouldBeLoadedNotRefreshing as MovieDetailState.Loaded).isRefreshing.not())

            viewModel.onRefresh(false)
            val shouldBeLoadedRefreshing = awaitItem()
            assert((shouldBeLoadedRefreshing as MovieDetailState.Loaded).isRefreshing)

            val shouldBeLoadedNotRefreshing2 = awaitItem()
            assert((shouldBeLoadedNotRefreshing2 as MovieDetailState.Loaded).isRefreshing.not())
        }
    }

    @Test
    fun errorMovieDetailShouldBeErrorState() = runTest {

        whenever(savedStateHandle.get<String>(Argument.MOVIE_ID_KEY)).thenReturn(
            MOVIE_ID
        )

        viewModel = MovieDetailViewModel(repository, savedStateHandle)
        whenever(repository.getMovieDetail(MOVIE_ID)).thenReturn(
            NetworkResponse.NetworkError(Throwable())
        )
        viewModel.detail.test {
            viewModel.getMovieDetail(MOVIE_ID)

            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is MovieDetailState.Loading)

            val shouldBeError = awaitItem()
            assert(shouldBeError is MovieDetailState.Error)
        }
    }
}