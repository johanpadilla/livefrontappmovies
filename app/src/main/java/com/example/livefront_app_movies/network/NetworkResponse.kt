package com.example.livefront_app_movies.network


sealed class NetworkResponse<out T> {
    data class Success<out T>(val body: T): NetworkResponse<T>()
    data object NetworkError: NetworkResponse<Nothing>()
}