package com.example.cinephile.presentation.pagination

/**
 * A data class that encapsulates the current state of paginated data loading, including page count and loading status.
 */
data class PaginationState(
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val isLoading: Boolean = false
) {
    val hasNextPage: Boolean
        get() = currentPage < totalPages
}