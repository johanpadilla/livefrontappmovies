package com.example.livefront_app_movies.viewmodel

import app.cash.turbine.test
import com.example.livefront_app_movies.ui.details.MovieDetailViewModel
import com.example.livefront_app_movies.CoroutineRuleDispatcher
import com.example.livefront_app_movies.model.movie_detail.MovieDetailRepository
import com.example.livefront_app_movies.model.movie_detail.MovieDetailResponse
import com.example.livefront_app_movies.network.NetworkResponse
import com.example.livefront_app_movies.ui.details.MovieDetail
import com.example.livefront_app_movies.ui.details.MovieDetailState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsServiceTest {

    private var repository: MovieDetailRepository = mock()

    private lateinit var viewModel: MovieDetailViewModel

    @get: Rule
    val coroutineRuleDispatcher = CoroutineRuleDispatcher()

    @Before
    fun setup() {
        viewModel = MovieDetailViewModel(repository, coroutineRuleDispatcher.testDispatcher)
    }


    @Test
    fun initialStateIsLoading() = runTest {
        advanceUntilIdle()
        val shouldBeLoading = viewModel.detail.first()

        assert(shouldBeLoading is MovieDetailState.Loading)
    }

    @Test
    fun nullMovieIdShouldBeErrorState() = runTest {
        advanceUntilIdle()

        viewModel.detail.test {
            viewModel.getMovieDetail(null)
            val resultState = expectMostRecentItem()
            assert(resultState is MovieDetailState.Error)
        }
    }

    @Test
    fun withMovieIdButNullBodyResponseShouldBeEmptyState() = runTest {
        advanceUntilIdle()
        val movieId = "12345-abc"
        whenever(repository.getMovieDetail(movieId)).thenReturn(
            NetworkResponse.Success(
                MovieDetailResponse()
            )
        )
        viewModel.detail.test {
            viewModel.getMovieDetail(movieId)

            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is MovieDetailState.Loading)

            val shouldBeEmpty = awaitItem()
            assert(shouldBeEmpty is MovieDetailState.Empty)
        }
    }

    @Test
    fun withMovieIdShouldBeLoadedState() = runTest {
        advanceUntilIdle()
        val movieId = "12345-abc"
        val title = "title"
        whenever(repository.getMovieDetail(movieId)).thenReturn(
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
            viewModel.getMovieDetail(movieId)

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
        advanceUntilIdle()
        val movieId = "12345-abc"
        val popularMovieResponse = MovieDetailResponse()

        whenever(repository.getMovieDetail(movieId)).thenReturn(
            NetworkResponse.Success(popularMovieResponse)
        )

        viewModel.detail.test {
            viewModel.getMovieDetail(movieId)

            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is MovieDetailState.Loading)
            val shouldBeEmpty = awaitItem()
            assert(shouldBeEmpty is MovieDetailState.Empty)

            viewModel.onRefresh(movieId, true)
            val shouldBeLoading2 = awaitItem()
            assert(shouldBeLoading2 is MovieDetailState.Loading)

            val shouldBeEmpty2 = awaitItem()
            assert(shouldBeEmpty2 is MovieDetailState.Empty)
        }
    }

    @Test
    fun withRestartResultsShouldBeLoadedState() = runTest {
        advanceUntilIdle()
        val movieId = "12345-abc"
        val title = "title"

        val popularMovieResponse = MovieDetailResponse(
            title = title
        )

        whenever(repository.getMovieDetail(movieId)).thenReturn(
            NetworkResponse.Success(popularMovieResponse)
        )

        viewModel.detail.test {
            viewModel.getMovieDetail(movieId)

            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is MovieDetailState.Loading)
            val shouldBeLoadedNotRefreshing = awaitItem()
            assert((shouldBeLoadedNotRefreshing as MovieDetailState.Loaded).isRefreshing.not())

            viewModel.onRefresh(movieId, false)
            val shouldBeLoadedRefreshing = awaitItem()
            assert((shouldBeLoadedRefreshing as MovieDetailState.Loaded).isRefreshing)

            val shouldBeLoadedNotRefreshing2 = awaitItem()
            assert((shouldBeLoadedNotRefreshing2 as MovieDetailState.Loaded).isRefreshing.not())
        }
    }

    @Test
    fun errorMovieDetailShouldBeErrorState() = runTest {
        advanceUntilIdle()
        val movieId = "12345-abc"
        whenever(repository.getMovieDetail(movieId)).thenReturn(
            NetworkResponse.NetworkError(Throwable())
        )
        viewModel.detail.test {
            viewModel.getMovieDetail(movieId)

            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is MovieDetailState.Loading)

            val shouldBeError = awaitItem()
            assert(shouldBeError is MovieDetailState.Error)
        }
    }
}