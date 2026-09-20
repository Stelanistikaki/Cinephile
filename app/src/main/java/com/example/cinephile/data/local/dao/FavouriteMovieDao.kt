package com.example.cinephile.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cinephile.data.local.entity.FavouriteMovieEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data access object providing methods for querying and modifying the favorite movies table in the local database.
 */

@Dao
interface FavouriteMovieDao {
    @Query("SELECT * FROM favorite_movies ORDER BY title ASC")
    fun getFavourites(): Flow<List<FavouriteMovieEntity>>
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    fun isFavourite(movieId: Int): Flow<Boolean>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(movie: FavouriteMovieEntity)
    @Delete
    suspend fun deleteFavourite(movie: FavouriteMovieEntity)
}