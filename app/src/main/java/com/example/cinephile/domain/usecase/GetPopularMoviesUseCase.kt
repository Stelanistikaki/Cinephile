package com.example.cinephile.domain.usecase

import com.example.cinephile.domain.model.Movie
import com.example.cinephile.domain.model.PaginatedResult
import com.example.cinephile.domain.repository.MovieRepository
import jakarta.inject.Inject

/**
 * A use case responsible for fetching a paginated list of popular movies from the repository.
 */

class GetPopularMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(
        page: Int
    ): PaginatedResult<Movie> {
        return repository.getPopularMovies(page)
    }
}