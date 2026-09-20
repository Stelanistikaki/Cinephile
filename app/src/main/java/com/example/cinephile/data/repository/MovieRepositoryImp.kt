package com.example.cinephile.data.repository

import com.example.cinephile.data.local.dao.FavouriteMovieDao
import com.example.cinephile.data.local.entity.toDomain
import com.example.cinephile.data.local.entity.toFavouriteEntity
import com.example.cinephile.data.remote.TheMovieDBApi
import com.example.cinephile.data.remote.dto.toDomain
import com.example.cinephile.domain.model.Movie
import com.example.cinephile.domain.model.MovieDetails
import com.example.cinephile.domain.model.PaginatedResult
import com.example.cinephile.domain.repository.MovieRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * The concrete implementation of MovieRepository that coordinates data flow between the remote API and the local database.
 */

class MovieRepositoryImpl @Inject constructor(
    private val api: TheMovieDBApi,
    private val favouriteMovieDao: FavouriteMovieDao
) : MovieRepository {

    override suspend fun getPopularMovies(
        page: Int
    ): PaginatedResult<Movie> {

        val response = api.getPopularMovies(
            page = page
        )

        return PaginatedResult(
            items = response.results.map { it.toDomain() },
            page = response.page,
            totalPages = response.totalPages
        )
    }

    override suspend fun searchMovies(
        query: String,
        page: Int
    ): PaginatedResult<Movie> {

        val response = api.searchMovies(
            query = query,
            page = page
        )

        return PaginatedResult(
            items = response.results.map { it.toDomain() },
            page = response.page,
            totalPages = response.totalPages
        )
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return api.getMovieDetails(movieId = movieId).toDomain()
    }

    override suspend fun addFavourite(movie: Movie) {
        favouriteMovieDao.insertFavourite(movie.toFavouriteEntity())
    }

    override suspend fun removeFavourite(movie: Movie) {
        favouriteMovieDao.deleteFavourite(movie.toFavouriteEntity())
    }

    override fun getFavourites(): Flow<List<Movie>> {
        return favouriteMovieDao.getFavourites().map { entity ->
            entity.map { it.toDomain() }
        }
    }

    override fun isFavourite(movieId: Int): Flow<Boolean> {
        return favouriteMovieDao.isFavourite(movieId)
    }
}