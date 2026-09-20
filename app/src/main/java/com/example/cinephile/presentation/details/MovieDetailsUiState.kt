package com.example.cinephile.presentation.details

import com.example.cinephile.domain.model.MovieDetails

/**
 * A data class representing the UI state for the movie details screen, including loading, data, and error states.
 */
data class MovieDetailUiState(
    val isLoading: Boolean = false,
    val movie: MovieDetails? = null,
    val error: String? = null
)