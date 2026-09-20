package com.example.cinephile.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cinephile.data.local.dao.FavouriteMovieDao
import com.example.cinephile.data.local.entity.FavouriteMovieEntity

/**
 * The Room database definition that manages local storage for the application.
 */

@Database(entities = [FavouriteMovieEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun favouriteMovieDao(): FavouriteMovieDao
}