package com.example.livefront_app_movies.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

suspend fun <T> performApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): NetworkResponse<T> {
    return withContext(dispatcher) {
        try {
            NetworkResponse.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            NetworkResponse.NetworkError
        }
    }
}