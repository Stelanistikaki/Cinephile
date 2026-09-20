package com.example.cinephile.data.di

import android.content.Context
import androidx.room.Room
import com.example.cinephile.data.local.MovieDatabase
import com.example.cinephile.data.local.dao.FavouriteMovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * A Hilt module that provides singleton instances of the Room database and its associated DAOs.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(
        @ApplicationContext context: Context
    ): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavouriteMovieDao(
        database: MovieDatabase
    ): FavouriteMovieDao {
        return database.favouriteMovieDao()
    }
}
