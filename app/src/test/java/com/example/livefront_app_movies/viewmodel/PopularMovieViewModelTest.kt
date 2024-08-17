package com.example.livefront_app_movies.viewmodel

import app.cash.turbine.test
import com.example.livefront_app_movies.CoroutineRuleDispatcher
import com.example.livefront_app_movies.model.popular_movie.PopularMovieRepository
import com.example.livefront_app_movies.model.popular_movie.PopularMovieResponse
import com.example.livefront_app_movies.model.popular_movie.Results
import com.example.livefront_app_movies.network.NetworkResponse
import com.example.livefront_app_movies.ui.home.PopularMovieState
import com.example.livefront_app_movies.ui.home.PopularMovieViewModel
import com.example.livefront_app_movies.ui.home.toPopularMovie
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PopularMovieViewModelTest {
    private val repository: PopularMovieRepository = mock()

    private lateinit var viewModel: PopularMovieViewModel

    @get: Rule
    val testCoroutineRule = CoroutineRuleDispatcher()

    @Before
    fun setup() {
        viewModel = PopularMovieViewModel(repository)
    }

    @Test
    fun initialStateIsLoading() = runTest {
        val shouldBeLoading = viewModel.popularMovies.first()

        assert(shouldBeLoading is PopularMovieState.Loading)
    }

    @Test
    fun loadedPopularMovieShouldBeLoadedState() = runTest {
        val popularMovieResponse = PopularMovieResponse(
            results = listOf(Results()), page = 0, totalPages = 0
        )
        whenever(repository.getPopularMovies()).thenReturn(
            NetworkResponse.Success(popularMovieResponse)
        )

        val loadedPopular = PopularMovieState.Loaded(
            totalPages = popularMovieResponse.totalPages,
            currentPage = popularMovieResponse.page,
            movies = popularMovieResponse.results.map { it.toPopularMovie() })

        viewModel.getPopularMovies()

        viewModel.popularMovies.test {
            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is PopularMovieState.Loading)
            val shouldBeLoaded = awaitItem()
            assert(shouldBeLoaded is PopularMovieState.Loaded)
            assert((shouldBeLoaded as PopularMovieState.Loaded).currentPage == loadedPopular.currentPage)
        }
    }

    @Test
    fun noResultsShouldBeEmptyState() = runTest {
        whenever(repository.getPopularMovies()).thenReturn(
            NetworkResponse.Success(PopularMovieResponse())
        )

        viewModel.popularMovies.test {
            viewModel.getPopularMovies()
            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is PopularMovieState.Loading)
            val shouldBeEmpty = awaitItem()
            assert(shouldBeEmpty is PopularMovieState.Empty)
        }
    }

    @Test
    fun withRestartNoResultsShouldBeEmptyState() = runTest {
        whenever(repository.getPopularMovies()).thenReturn(
            NetworkResponse.Success(PopularMovieResponse())
        )

        viewModel.popularMovies.test {
            viewModel.getPopularMovies()

            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is PopularMovieState.Loading)
            val shouldBeEmpty = awaitItem()
            assert(shouldBeEmpty is PopularMovieState.Empty)

            viewModel.refresh(true)
            val shouldBeLoading2 = awaitItem()
            assert(shouldBeLoading2 is PopularMovieState.Loading)
            val shouldBeEmpty2 = awaitItem()
            assert(shouldBeEmpty2 is PopularMovieState.Empty)
        }
    }

    @Test
    fun withRestartResultsShouldBeLoadedState() = runTest {
        val popularMovieResponse = PopularMovieResponse(
            results = listOf(Results()), page = 0, totalPages = 0
        )

        whenever(repository.getPopularMovies()).thenReturn(
            NetworkResponse.Success(popularMovieResponse)
        )

        viewModel.popularMovies.test {
            viewModel.getPopularMovies()

            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is PopularMovieState.Loading)
            val shouldBeLoadedNotRefreshing = awaitItem()
            assert((shouldBeLoadedNotRefreshing as PopularMovieState.Loaded).isRefreshing.not())

            viewModel.refresh(false)
            val shouldBeLoadedRefreshing = awaitItem()
            assert((shouldBeLoadedRefreshing as PopularMovieState.Loaded).isRefreshing)

            val shouldBeLoadedNotRefreshing2 = awaitItem()
            assert((shouldBeLoadedNotRefreshing2 as PopularMovieState.Loaded).isRefreshing.not())
        }
    }

    @Test
    fun errorPopularMovieShouldBeErrorState() = runTest {
        whenever(repository.getPopularMovies()).thenReturn(
            NetworkResponse.NetworkError(Throwable())
        )

        viewModel.popularMovies.test {
            viewModel.getPopularMovies()
            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is PopularMovieState.Loading)
            val shouldBeError = awaitItem()
            assert(shouldBeError is PopularMovieState.Error)
        }
    }
}