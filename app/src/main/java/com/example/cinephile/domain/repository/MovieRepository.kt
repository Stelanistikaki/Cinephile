package com.example.cinephile.domain.repository

import com.example.cinephile.domain.model.Movie
import com.example.cinephile.domain.model.MovieDetails
import com.example.cinephile.domain.model.PaginatedResult
import kotlinx.coroutines.flow.Flow

/**
 * An interface defining the core data operations for the application, abstracting the data source from the domain layer.
 */

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): PaginatedResult<Movie>
    suspend fun getMovieDetails(movieId: Int): MovieDetails
    suspend fun searchMovies(query: String, page: Int): PaginatedResult<Movie>
    suspend fun addFavourite(movie: Movie)
    suspend fun removeFavourite(movie: Movie)
    fun getFavourites(): Flow<List<Movie>>
    fun isFavourite(movieId: Int): Flow<Boolean>
}