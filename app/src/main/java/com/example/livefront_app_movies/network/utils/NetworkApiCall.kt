package com.example.livefront_app_movies.network.utils

import com.example.livefront_app_movies.network.NetworkResponse
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend fun <T> performApiCall(
    dispatcher: CoroutineContext,
    apiCall: suspend () -> T
): NetworkResponse<T> {
    return withContext(dispatcher) {
        try {
            NetworkResponse.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            NetworkResponse.NetworkError
        }
    }
}