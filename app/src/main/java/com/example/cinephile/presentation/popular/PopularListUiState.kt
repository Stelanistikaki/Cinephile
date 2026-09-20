package com.example.cinephile.presentation.popular

import com.example.cinephile.domain.model.Movie
import com.example.cinephile.presentation.error.MovieError
import com.example.cinephile.presentation.pagination.PaginationState

/**
 * Represents the UI state for the popular movies list screen, including pagination and favorite status tracking.
 */
data class PopularListUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: MovieError? = null,
    val pagination: PaginationState = PaginationState(),
    val favouriteMovieIds: Set<Int> = emptySet()
)