package com.example.livefront_app_movies

import com.example.livefront_app_movies.model.PopularMovieResponse
import com.example.livefront_app_movies.model.PopularMovieResponseJsonAdapter
import com.example.livefront_app_movies.model.Results
import com.example.livefront_app_movies.network.MovieService
import com.example.livefront_app_movies.ui.home.HomeState
import com.example.livefront_app_movies.ui.home.HomeViewModel
import com.squareup.moshi.Moshi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.anyInt
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PopularMovieViewModelTest {
    private lateinit var  remoteSource: MovieService

    private lateinit var subject: HomeViewModel

    private lateinit var mockWebServer: MockWebServer
    private lateinit var moshi: Moshi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        remoteSource = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient())
            .build().create(MovieService::class.java)
        subject = HomeViewModel(remoteSource)
        moshi = Moshi.Builder().build()

    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testSuccessResponseWithEmptyBody() = runTest{
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(PopularMovieResponseJsonAdapter(moshi).toJson(PopularMovieResponse()))
        mockWebServer.enqueue(response)

        subject.getMovies(anyInt())

        println(subject.movies.value)
        assert(subject.movies.value is HomeState.Empty)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testErrorNetworkError() = runTest{
        val response = MockResponse()
            .setResponseCode(400)
            .setBody(PopularMovieResponseJsonAdapter(moshi).toJson(PopularMovieResponse()))
        mockWebServer.enqueue(response)

        subject.getMovies(anyInt())
        advanceUntilIdle()
        println(subject.movies.value)
        assert(subject.movies.value is HomeState.Error)

    }

    @Test
    fun testSuccessNetworkLoadedState() = runTest{
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(PopularMovieResponseJsonAdapter(moshi).toJson(PopularMovieResponse(results = listOf(
                Results()
            ))))
        mockWebServer.enqueue(response)

        subject.getMovies(anyInt())
        println(subject.movies.value)
        assert(subject.movies.value is HomeState.Loaded)

    }
}