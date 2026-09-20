package com.example.cinephile.presentation.error

import retrofit2.HttpException
import java.io.IOException

/**
 * Defines a sealed interface for movie-related error types and an extension function to map exceptions to these errors.
 */

sealed interface MovieError {
    data object NoInternet : MovieError
    data object Server : MovieError
    data object Unknown : MovieError
}

fun Throwable.toPopularListError(): MovieError {
    return when (this) {
        is IOException -> MovieError.NoInternet
        is HttpException -> MovieError.Server
        else -> MovieError.Unknown
    }
}