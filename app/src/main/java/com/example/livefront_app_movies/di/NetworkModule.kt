package com.example.livefront_app_movies.di

import com.example.livefront_app_movies.BuildConfig
import com.example.livefront_app_movies.network.interceptors.AuthenticationInterceptor
import com.example.livefront_app_movies.network.movie.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun authenticationInterceptor(@Named("accessToken") accessToken: String): AuthenticationInterceptor =
        AuthenticationInterceptor(accessToken)


    @Provides
    fun provideOkHppClient(authenticationInterceptor: AuthenticationInterceptor) =
        OkHttpClient.Builder()
            .addNetworkInterceptor(authenticationInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("baseUrl") baseUrl: String,
        okkHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .client(
            okkHttpClient
        )
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    fun provideMovieService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

}

@Module
@InstallIn(SingletonComponent::class)
object UrlModule {
    @Named("baseUrl")
    @Provides
    fun provideBaseUrl(): String = BuildConfig.API_URL

    @Named("accessToken")
    @Provides
    fun provideAccessToken(): String = BuildConfig.ACCESS_TOKEN
}