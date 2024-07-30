package com.example.livefront_app_movies.di

import com.example.livefront_app_movies.network.movie.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideBaseUrl(): String = "https://api.themoviedb.org/"

    @Provides
    fun authenticationInterceptor(): AuthenticationInterceptor = AuthenticationInterceptor()

    @Provides
    @Singleton
    fun provideRetrofit(
        baseUrl: String,
        authenticationInterceptor: AuthenticationInterceptor
    ): Retrofit = Retrofit.Builder()
        .client(
            OkHttpClient.Builder()
                .addNetworkInterceptor(authenticationInterceptor)
                .build()
        )
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    fun provideMovieService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

}