package com.example.cinephile.domain.model

/**
 * A domain model representing basic movie information used throughout the application.
 */

data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val releaseDate: String?,
    val rating: Double
)