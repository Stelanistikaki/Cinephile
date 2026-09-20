package com.example.cinephile.domain.model

/**
 * A generic container for paginated data, holding a list of items and current pagination metadata.
 */
data class PaginatedResult<T>(
    val items: List<T>,
    val page: Int,
    val totalPages: Int
)