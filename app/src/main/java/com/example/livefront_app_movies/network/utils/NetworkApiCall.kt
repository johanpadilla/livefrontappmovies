package com.example.livefront_app_movies.network.utils

import com.example.livefront_app_movies.network.NetworkResponse
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Suspend function which provides a wrapper for the Network Response status of an http call.
 * @param dispatcher - CoroutineContext type
 * @param apiCall - Suspended function which will be executed.
 * @return NetworkResponse (Success|NetworkError) depending of the http call response.
 */
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