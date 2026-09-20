package com.example.cinephile.domain.usecase

import com.example.cinephile.domain.model.Movie
import com.example.cinephile.domain.model.PaginatedResult
import com.example.cinephile.domain.repository.MovieRepository
import jakarta.inject.Inject

/**
 * A use case responsible for searching for movies based on a query string and page number.
 */
class GetSearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(
        query: String,
        page: Int
    ): PaginatedResult<Movie> {
        return repository.searchMovies(query, page)
    }
}