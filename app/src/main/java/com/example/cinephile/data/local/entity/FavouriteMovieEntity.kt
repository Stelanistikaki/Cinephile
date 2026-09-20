package com.example.cinephile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a movie record in the local database's favorite movies table.
 */

@Entity(tableName = "favorite_movies")
data class FavouriteMovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String?,
    val releaseDate: String?,
    val rating: Double
)