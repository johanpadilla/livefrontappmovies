package com.example.livefront_app_movies.viewmodel

import app.cash.turbine.test
import com.example.livefront_app_movies.model.MovieDetailResponse
import com.example.livefront_app_movies.network.movie.MovieService
import com.example.livefront_app_movies.ui.details.MovieDetail
import com.example.livefront_app_movies.ui.details.MovieDetailState
import com.example.livefront_app_movies.ui.details.MovieDetailViewModel
import com.example.livefront_app_movies.TestCoroutineRule
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

    private var remoteSource: MovieService = mock()
    private lateinit var viewModel: MovieDetailViewModel

    @get: Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        viewModel = MovieDetailViewModel(remoteSource, testCoroutineRule.testDispatcher, )

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
        whenever(remoteSource.getMovieDetail(movieId)).thenReturn(
            MovieDetailResponse(
                title = title
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
}