package com.example.cinephile.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Represents the paginated response structure from TMDB's movie listing and search endpoints.
 */

data class PopularMoviesResponse(
    val page: Int,
    val results: List<MovieDto>,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("total_results")
    val totalResults: Int
)