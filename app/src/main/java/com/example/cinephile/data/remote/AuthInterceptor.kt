package com.example.cinephile.data.remote

import jakarta.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

/**
 * An OkHttp interceptor that adds the TMDB API token to the authorization header of every outgoing request.
 */

class AuthInterceptor @Inject constructor(private val apiToken: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(
                "Authorization",
                "Bearer $apiToken"
            )
            .build()

        return chain.proceed(request)
    }
}