package com.example.livefront_app_movies.network

/**
 * Network Response Wrapper.
 */
sealed class NetworkResponse<out A, out B> {
    data class Success<out A>(val body: A): NetworkResponse<A, Nothing>()
    data class NetworkError<out B>(val error: Throwable?): NetworkResponse<B, Nothing>()
}