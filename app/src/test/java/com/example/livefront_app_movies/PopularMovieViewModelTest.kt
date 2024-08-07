package com.example.livefront_app_movies

import app.cash.turbine.test
import com.example.livefront_app_movies.model.PopularMovieResponse
import com.example.livefront_app_movies.model.Results
import com.example.livefront_app_movies.network.movie.MovieService
import com.example.livefront_app_movies.ui.home.HomeState
import com.example.livefront_app_movies.ui.home.HomeViewModel
import com.example.livefront_app_movies.ui.home.toPopularMovie
import com.example.livefront_app_movies.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.anyInt
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class PopularMovieViewModelTest {
    private val remoteSource: MovieService = mock()
    //private val networkApiCall: NetworkApiCall = mockk(relaxed = true)

    private lateinit var viewModel: HomeViewModel


    @get: Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        viewModel = HomeViewModel(remoteSource, testCoroutineRule.testDispatcher)
    }


    @Test
    fun initialStateIsLoading() = runTest {
        advanceUntilIdle()
        val shouldBeLoading = viewModel.movies.first()

        assert(shouldBeLoading is HomeState.Loading)
    }

    @Test
    fun loadedPopularMovieShouldBeLoadedState() = runTest {
        advanceUntilIdle()
        val page = 1
        val popularMovieResponse = PopularMovieResponse(
            results = listOf(Results()), page = 0, totalPages = 0
        )
        val params = mutableMapOf(("language" to "en-US"))
        params.putAll(mapOf(("page" to page.toString())))
        whenever(remoteSource.getPopularMovies(params)).thenReturn(
            popularMovieResponse
        )

        val loadedPopular = HomeState.Loaded(
            totalPages = popularMovieResponse.totalPages,
            currentPage = popularMovieResponse.page,
            movies = popularMovieResponse.results.map { it.toPopularMovie() })

        viewModel.movies.test {
            viewModel.getMovies(page)
            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is HomeState.Loading)
            val shouldBeLoaded = awaitItem()
            assert(shouldBeLoaded is HomeState.Loaded)
            assert((shouldBeLoaded as HomeState.Loaded).currentPage == loadedPopular.currentPage)
        }
    }

    @Test
    fun noResultsShouldBeEmptyState() = runTest {
        advanceUntilIdle()
        val page = anyInt()
        val params = mutableMapOf(("language" to "en-US"))
        params.putAll(mapOf(("page" to page.toString())))



        viewModel.movies.test {

            viewModel.getMovies(page)
            val shouldBeLoading = awaitItem()
            assert(shouldBeLoading is HomeState.Loading)
            val shouldBeEmpty = awaitItem()
            assert(shouldBeEmpty is HomeState.Empty)
        }
    }

    /*    private lateinit var remoteSource: MovieService

        private lateinit var mockWebServer: MockWebServer
        private lateinit var moshi: Moshi

        private val dispatchers = StandardTestDispatcher()

        @OptIn(ExperimentalCoroutinesApi::class)
        @Before
        fun setup() {
            Dispatchers.setMain(dispatchers)
            mockWebServer = MockWebServer()
            mockWebServer.start()
            remoteSource = Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(MoshiConverterFactory.create())
                .client(OkHttpClient())
                .build().create(MovieService::class.java)

            moshi = Moshi.Builder().build()

        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @After
        fun teardown() {
            mockWebServer.shutdown()
            Dispatchers.resetMain()
        }

        @Test
        fun testSuccessResponseWithEmptyBody() = runTest(dispatchers) {
            val subject = HomeViewModel(remoteSource, dispatchers)
            val response = MockResponse()
                .setResponseCode(200)
                .setBody(PopularMovieResponseJsonAdapter(moshi).toJson(PopularMovieResponse()))
            mockWebServer.enqueue(response)
            subject.getMovies(anyInt())

            assert(subject.movies.value is HomeState.Empty)
        }

        @Test
        fun testErrorNetworkError() = runTest(dispatchers) {
            val subject = HomeViewModel(remoteSource, dispatchers)

            val response = MockResponse()
                .setResponseCode(400)
                .setBody(PopularMovieResponseJsonAdapter(moshi).toJson(PopularMovieResponse()))
            mockWebServer.enqueue(response)
            subject.getMovies(anyInt())
            assert(subject.movies.value is HomeState.Error)

        }

        @Test
        fun testSuccessNetworkLoadedState() = runTest(dispatchers) {
            val subject = HomeViewModel(remoteSource, dispatchers)
            val response = MockResponse()
                .setResponseCode(200)
                .setBody(
                    PopularMovieResponseJsonAdapter(moshi).toJson(
                        PopularMovieResponse(
                            results = listOf(
                                Results()
                            )
                        )
                    )
                )
            mockWebServer.enqueue(response)
            subject.getMovies(anyInt())
            assert(subject.movies.value is HomeState.Loaded)
        }*/
}