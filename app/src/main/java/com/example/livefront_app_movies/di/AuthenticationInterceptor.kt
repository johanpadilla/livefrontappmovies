package com.example.livefront_app_movies.di

import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("accept", "application/json")
            .addHeader(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlZmFkNDAyZWIyMDBhYWI1NTA1ZWE5MzkyMzljZThmMSIsIm5iZiI6MTcyMjI3Mjc1Ni4yMTE4NjQsInN1YiI6IjYwMDFhMzJmY2IzMDg0MDAzYmQ4NDM1YyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.PrEfSHlx5cOef-MTpJlSW9yMdpxi6tCaYZiy4EA5DVc"
            )
            .build()
        return chain.proceed(newRequest)
    }

}