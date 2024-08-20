package com.example.livefront_app_movies.ui.home

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.livefront_app_movies.MainActivity
import com.example.livefront_app_movies.di.NetworkModule
import com.example.livefront_app_movies.di.UrlModule
import com.example.livefront_app_movies.model.popular_movie.PopularMovieResponse
import com.example.livefront_app_movies.model.popular_movie.createPopularMovieResponse
import com.example.livefront_app_movies.network.interceptors.AuthenticationInterceptor
import com.example.livefront_app_movies.network.movie.MovieService
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * References used:
 * https://github.com/tugceaktepe/PagingMovieExample
 * https://github.com/iago001/CustomerListApp
 * https://medium.com/@gary.chang/jetpack-compose-android-testing-beyond-the-basics-b27ced6c543e
 * https://medium.com/@tugceaktepe/building-instrumented-tests-with-mockwebserver-in-android-ea386a66c142
 */

@UninstallModules(
    UrlModule::class,
    NetworkModule::class
)
@HiltAndroidTest
class PopularMovieScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get: Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    val moshi: Moshi = Moshi.Builder().build()
    @OptIn(ExperimentalStdlibApi::class)
    val jsonAdapter: JsonAdapter<PopularMovieResponse> = moshi.adapter<PopularMovieResponse>()

    companion object {
        lateinit var baseUrl: String
        lateinit var mockWebServer: MockWebServer


        @BeforeClass
        @JvmStatic
        fun setup() {
            mockWebServer = MockWebServer()
            mockWebServer.start()
            baseUrl = mockWebServer.url("").toString()
        }
    }


    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun activityShouldBeLaunchedAndLoaded() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(jsonAdapter.toJson(createPopularMovieResponse())))
        composeRule.onNodeWithTag("popular_movie_container").assertExists()
    }

    @Test
    fun activityShouldBeLaunchedWithError() {
        mockWebServer.enqueue(MockResponse().setResponseCode(500).setBody(""))
        composeRule.onNodeWithTag("popular_movie_error_container").assertExists()
    }





    @Module
    @InstallIn(SingletonComponent::class)
    class FakeNetworkModule {

        @Provides
        fun authenticationInterceptor(@Named("accessToken") accessToken: String): AuthenticationInterceptor =
            AuthenticationInterceptor(accessToken)


        @Provides
        fun provideOkHppClient(authenticationInterceptor: AuthenticationInterceptor) =
            OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(authenticationInterceptor)
                .build()


        @Provides
        @Singleton
        @Named("baseUrl")
        fun provideUrl(): String = "http://localhost/"


        @Provides
        @Singleton
        @Named("accessToken")
        fun provideAccessToken(): String = "Bearer ACCESS TOKEN"

        @Provides
        @Singleton
        fun provideRetrofit(
            okkHttpClient: OkHttpClient
        ): Retrofit = Retrofit.Builder()
            .client(
                okkHttpClient
            )
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        @Provides
        @Singleton
        fun provideMovieService(retrofit: Retrofit): MovieService =
            retrofit.create(MovieService::class.java)

    }
}