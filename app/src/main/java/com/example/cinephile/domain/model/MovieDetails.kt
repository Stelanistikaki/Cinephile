package com.example.cinephile.domain.model

/**
 * A domain model representing comprehensive details for a specific movie, used in the details screen.
 */

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val rating: Double,
    val runtime: Int?,
    val genres: List<String>
)