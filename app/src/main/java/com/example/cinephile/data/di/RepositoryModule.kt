package com.example.cinephile.data.di

import com.example.cinephile.data.repository.MovieRepositoryImpl
import com.example.cinephile.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * A Hilt module that binds the MovieRepository interface to its concrete implementation.
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        implementation: MovieRepositoryImpl
    ): MovieRepository
}