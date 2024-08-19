package com.example.livefront_app_movies.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Authentication Interceptor used to add the ACCESS_TOKEN into the network header.
 */
class AuthenticationInterceptor @Inject constructor(
    private val accessToken: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("accept", "application/json")
            .addHeader(
                "Authorization",
                "Bearer $accessToken"
            )
            .build()
        return chain.proceed(newRequest)
    }
}