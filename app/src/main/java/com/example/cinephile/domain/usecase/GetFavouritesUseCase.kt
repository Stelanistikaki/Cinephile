package com.example.cinephile.domain.usecase

import com.example.cinephile.domain.model.Movie
import com.example.cinephile.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * A use case that manages retrieving and toggling the favorite status of movies.
 */

class GetFavouritesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    fun getFavourites(): Flow<List<Movie>> {
        return repository.getFavourites()
    }

    suspend fun toggleFavourite(movie: Movie, isFavorite: Boolean) {
        if (isFavorite) {
            repository.removeFavourite(movie)
        } else {
            repository.addFavourite(movie)
        }
    }
}